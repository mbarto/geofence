/*
 *  Copyright (C) 2007 - 2012 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 * 
 *  GPLv3 + Classpath exception
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.geofence.cache;

import it.geosolutions.geofence.config.GeoFencePropertyPlaceholderConfigurer;
import it.geosolutions.geofence.services.RuleReaderService;
import it.geosolutions.geofence.services.dto.AccessInfo;
import it.geosolutions.geofence.services.dto.AuthUser;
import it.geosolutions.geofence.services.dto.RuleFilter;
import it.geosolutions.geofence.services.dto.ShortRule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * A delegating {@link it.geosolutions.georepo.services.RuleReaderService} with caching capabilities.
 * <P/>
 * Cache eviction policy is LRU.<br/>
 * Cache coherence is handled by entry timeout.<br/>
 * <p/>
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class CachedRuleReader implements RuleReaderService {

    static final Logger LOGGER = Logging.getLogger(CachedRuleReader.class);

    private RuleReaderService realRuleReaderService;

    private LoadingCache<RuleFilter, AccessInfo> ruleCache;
    private LoadingCache<NamePw, AuthUser>       userCache;

    private final CacheInitParams cacheInitParams = new CacheInitParams();

    GeoFencePropertyPlaceholderConfigurer configurer;
    
    public CachedRuleReader(GeoFencePropertyPlaceholderConfigurer configurer) {
        this.configurer = configurer;
    }

    /**
     * Init the cache, using the provided init params.
     * Please use {@link #getCacheInitParams() } to set the cache parameters before
     * <code>init()</code>ting the cache
     */
    public void init() {
        ruleCache  = getCacheBuilder().build(new RuleLoader());
        userCache = getCacheBuilder().build(new UserLoader());
    }

    protected CacheBuilder getCacheBuilder() {
        CacheBuilder builder = CacheBuilder.newBuilder()
                .maximumSize(cacheInitParams.getSize())
                .refreshAfterWrite(cacheInitParams.getRefreshMilliSec(), TimeUnit.MILLISECONDS) // reloadable after x time
                .expireAfterWrite(cacheInitParams.getExpireMilliSec(), TimeUnit.MILLISECONDS) // throw away entries too old
                ;
        //.expireAfterAccess(timeoutMillis, TimeUnit.MILLISECONDS)
        //                .removalListener(MY_LISTENER)
        // this should only be used while testing
        if(cacheInitParams.getCustomTicker() != null) {
            LOGGER.log(Level.SEVERE, "Setting a custom Ticker in the cache {0}", cacheInitParams.getCustomTicker().getClass().getName());
            builder.ticker(cacheInitParams.getCustomTicker());
        }
        return builder;
    }



    private class RuleLoader extends CacheLoader<RuleFilter, AccessInfo> {

        @Override
        public AccessInfo load(RuleFilter filter) throws Exception {
            if(LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Loading {0}", filter);
            return realRuleReaderService.getAccessInfo(filter);
        }

        @Override
        public ListenableFuture<AccessInfo> reload(final RuleFilter filter, AccessInfo accessInfo) throws Exception {
            if(LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Reloading {0}", filter);

            // this is a sync implementation
            AccessInfo ret = realRuleReaderService.getAccessInfo(filter);
            return Futures.immediateFuture(ret);

            // next there is an asynchronous implementation, but in tests it seems to hang
//            return ListenableFutureTask.create(new Callable<AccessInfo>() {
//                @Override
//                public AccessInfo call() throws Exception {
//                    if(LOGGER.isLoggable(Level.FINE))
//                        LOGGER.log(Level.FINE, "Asynch reloading {0}", filter);
//                    return realRuleReaderService.getAccessInfo(filter);
//                }
//            });
        }
    }

    private class UserLoader extends CacheLoader<NamePw, AuthUser> {

        @Override
        public AuthUser load(NamePw user) throws NoAuthException {
            if(LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Loading user '"+user.getName()+"'");
            AuthUser auth = realRuleReaderService.authorize(user.getName(), user.getPw());
            if(auth==null)
                throw new NoAuthException("Can't auth user ["+user.getName()+"]");
            return auth;
        }

        @Override
        public ListenableFuture<AuthUser> reload(final NamePw user, AuthUser authUser) throws NoAuthException {
            if(LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Reloading user '"+user.getName()+"'");

            // this is a sync implementation
            AuthUser auth = realRuleReaderService.authorize(user.getName(), user.getPw());
            if(auth==null)
                throw new NoAuthException("Can't auth user ["+user.getName()+"]");
            return Futures.immediateFuture(auth);

            // todo: we may want a asynchronous implementation
        }
    }

    public void invalidateAll() {
        if(LOGGER.isLoggable(Level.WARNING))
            LOGGER.log(Level.WARNING, "Forcing cache invalidation");
        ruleCache.invalidateAll();
        userCache.invalidateAll();
    }

    /**
     * <B>Deprecated method are not cached.</B>
     *
     * @deprecated Use {@link #getAccessInfo(RuleFilter filter) }
     */
    @Override
    public AccessInfo getAccessInfo(String userName, String profileName, String instanceName, String service, String request, String workspace, String layer) {
        LOGGER.severe("DEPRECATED METHODS ARE NOT CACHED");
        return realRuleReaderService.getAccessInfo(userName, profileName, instanceName, service, request, workspace, layer);
    }

    private AtomicLong dumpCnt = new AtomicLong(0);

    @Override
    public AccessInfo getAccessInfo(RuleFilter filter) {
        if(LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Request for {0}", filter);

        if(LOGGER.isLoggable(Level.INFO))
            if(dumpCnt.incrementAndGet() % 10 == 0) {
                LOGGER.info("Rules  :"+ruleCache.stats());
                LOGGER.info("Users  :"+userCache.stats());
                LOGGER.fine("params :"+cacheInitParams);
            }

        try {
            return ruleCache.get(filter);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex); // fixme: handle me
        }
    }

    /**
     * <B>Deprecated method are not cached.</B>
     *
     * @deprecated Use {@link #getMatchingRules(RuleFilter filter) }
     */
    @Override
    public List<ShortRule> getMatchingRules(String userName, String profileName, String instanceName, String service, String request, String workspace, String layer) {
        LOGGER.severe("DEPRECATED METHODS ARE NOT CACHED");
        return realRuleReaderService.getMatchingRules(userName, profileName, instanceName, service, request, workspace, layer);
    }

    @Override
    public List<ShortRule> getMatchingRules(RuleFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AuthUser authorize(String username, String password) {
        try {
            return userCache.get(new NamePw(username, password));
//        } catch (NoAuthException ex) {
//            LOGGER.warning(ex.getMessage());
//            return null;
        } catch (ExecutionException ex) {
            LOGGER.warning(ex.getMessage());
            return null;
        }
        
    }


    //--------------------------------------------------------------------------
    public void setRealRuleReaderService(RuleReaderService realRuleReaderService) {
        this.realRuleReaderService = realRuleReaderService;
    }

    

    public RuleReaderService getRealRuleReaderService() {
        return realRuleReaderService;
    }

    public CacheInitParams getCacheInitParams() {
        return cacheInitParams;
    }

    public CacheStats getStats() {
        return ruleCache.stats();
    }

    public CacheStats getUserStats() {
        return userCache.stats();
    }

    public long getCacheSize() {
        return ruleCache.size();
    }

    public long getUserCacheSize() {
        return userCache.size();
    }

    /**
     * May be useful if an external peer doesn't want to use the guava dep.
     */
    public String getStatsString() {
        return ruleCache.stats().toString();
    }

    

    @Override
    public String toString() {
        return getClass().getSimpleName()
                +"["
                + "Rule:"+ruleCache.stats()
                + " User:"+userCache.stats()
                + " " + cacheInitParams
                + "]";
    }

    protected static class NamePw {
        private String name;
        private String pw;

        public NamePw() {
        }

        public NamePw(String name, String pw) {
            this.name = name;
            this.pw = pw;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPw() {
            return pw;
        }

        public void setPw(String pw) {
            this.pw = pw;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
            hash = 89 * hash + (this.pw != null ? this.pw.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NamePw other = (NamePw) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            if ((this.pw == null) ? (other.pw != null) : !this.pw.equals(other.pw)) {
                return false;
            }
            return true;
        }
    }

    class NoAuthException extends Exception {

        public NoAuthException() {
        }

        public NoAuthException(String message) {
            super(message);
        }

        public NoAuthException(String message, Throwable cause) {
            super(message, cause);
        }

        public NoAuthException(Throwable cause) {
            super(cause);
        }
    
    }

    /**
     * @param params
     * @throws IOException 
     */
    public void saveConfiguration(CacheInitParams params) throws IOException {
        cacheInitParams.setSize(params.getSize());
        cacheInitParams.setRefreshMilliSec(params.getRefreshMilliSec());
        cacheInitParams.setExpireMilliSec(params.getExpireMilliSec());
        
        init();
        
        File configurationFile =  configurer.getConfigFile();
        if (configurationFile != null && configurationFile.exists()
                && configurationFile.canWrite()) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(configurationFile));
                writer.write("cacheSize=" + params.getSize()
                        + "\n");
                writer.write("cacheRefresh=" + params.getRefreshMilliSec()
                        + "\n");
                writer.write("cacheExpire=" + params.getExpireMilliSec() 
                        + "\n");
                
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } else {
            throw new IOException("Cannot save GeoFence cache configuration file");
        }
    }

 
}
