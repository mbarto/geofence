/*
 * $ Header: it.geosolutions.geofence.gui.client.icons.GeofenceIcons,v. 0.1 31-gen-2011 12.46.28 created by afabiani <alessio.fabiani at geo-solutions.it> $
 * $ Revision: 0.1 $
 * $ Date: 31-gen-2011 12.46.28 $
 *
 * ====================================================================
 *
 * Copyright (C) 2007 - 2011 GeoSolutions S.A.S.
 * http://www.geo-solutions.it
 *
 * GPLv3 + Classpath exception
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. 
 *
 * ====================================================================
 *
 * This software consists of voluntary contributions made by developers
 * of GeoSolutions.  For more information on GeoSolutions, please see
 * <http://www.geo-solutions.it/>.
 *
 */
package it.geosolutions.geofence.gui.client.icons;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

// TODO: Auto-generated Javadoc
/**
 * The Interface GeofenceIcons.
 */
@SuppressWarnings("deprecation")
public interface GeofenceIcons extends ImageBundle {

    /**
     * Adds the.
     * 
     * @return the abstract image prototype
     */
    @Resource("add.gif")
    AbstractImagePrototype add();

    /**
     * Arrow down.
     * 
     * @return the abstract image prototype
     */
    @Resource("arrow_down.png")
    AbstractImagePrototype arrowDown();
    
    /**
     * Arrow up.
     * 
     * @return the abstract image prototype
     */
    @Resource("arrow_up.png")
    AbstractImagePrototype arrowUp();
    
    /**
     * Page edit.
     * 
     * @return the abstract image prototype
     */
    @Resource("page_edit.gif")
    AbstractImagePrototype pageEdit();

    /**
     * Table.
     * 
     * @return the abstract image prototype
     */
    @Resource("table.png")
    AbstractImagePrototype table();

    /**
     * Connect.
     * 
     * @return the abstract image prototype
     */
    @Resource("connect.png")
    AbstractImagePrototype connect();

    /**
     * User add.
     * 
     * @return the abstract image prototype
     */
    @Resource("user_add.png")
    AbstractImagePrototype userAdd();

    /**
     * User delete.
     * 
     * @return the abstract image prototype
     */
    @Resource("user_delete.png")
    AbstractImagePrototype userDelete();

    /**
     * Delete.
     * 
     * @return the abstract image prototype
     */
    @Resource("delete.gif")
    AbstractImagePrototype delete();

    /**
     * User.
     * 
     * @return the abstract image prototype
     */
    @Resource("user.gif")
    AbstractImagePrototype user();

    /**
     * Zoom in.
     * 
     * @return the abstract image prototype
     */
    @Resource("zoom-in.png")
    AbstractImagePrototype zoomIn();

    /**
     * Zoom out.
     * 
     * @return the abstract image prototype
     */
    @Resource("zoom-out.png")
    AbstractImagePrototype zoomOut();

    /**
     * Info.
     * 
     * @return the abstract image prototype
     */
    @Resource("geofence-about.png")
    AbstractImagePrototype info();

    /**
     * Draw feature.
     * 
     * @return the abstract image prototype
     */
    @Resource("draw-feature.png")
    AbstractImagePrototype drawFeature();

    /**
     * Upload shp.
     * 
     * @return the abstract image prototype
     */
    @Resource("file_upload_icon.png")
    AbstractImagePrototype uploadSHP();

    /**
     * Clean dg watch menu.
     * 
     * @return the abstract image prototype
     */
    @Resource("eraser_minus.png")
    AbstractImagePrototype cleanGeofenceMenu();

    /**
     * Logout.
     * 
     * @return the abstract image prototype
     */
    @Resource("logout.png")
    AbstractImagePrototype logout();

    /**
     * Adds the aoi.
     * 
     * @return the abstract image prototype
     */
    @Resource("aoi_add.png")
    AbstractImagePrototype addAOI();

    /**
     * Delete aoi.
     * 
     * @return the abstract image prototype
     */
    @Resource("aoi_del.png")
    AbstractImagePrototype deleteAOI();

    /**
     * Edits the aoi.
     * 
     * @return the abstract image prototype
     */
    @Resource("aoi_edit.png")
    AbstractImagePrototype editAOI();

    /**
     * Rss.
     * 
     * @return the abstract image prototype
     */
    @Resource("rss.png")
    AbstractImagePrototype rss();

    /**
     * Edits the user.
     * 
     * @return the abstract image prototype
     */
    @Resource("user_edit.png")
    AbstractImagePrototype editUser();

    /**
     * Gets the aOIS.
     * 
     * @return the aOIS
     */
    @Resource("aoi_get.png")
    AbstractImagePrototype getAOIS();

    /**
     * Gets the features.
     * 
     * @return the features
     */
    @Resource("features_get.png")
    AbstractImagePrototype getFeatures();

    /**
     * Search.
     * 
     * @return the abstract image prototype
     */
    @Resource("find.png")
    AbstractImagePrototype search();

    /**
     * Share.
     * 
     * @return the abstract image prototype
     */
    @Resource("share.png")
    AbstractImagePrototype share();

    /**
     * Save.
     * 
     * @return the abstract image prototype
     */
    @Resource("save.png")
    AbstractImagePrototype save();

    /**
     * Trigger.
     * 
     * @return the abstract image prototype
     */
    @Resource("trigger.png")
    AbstractImagePrototype trigger();

    /**
     * Search watches.
     * 
     * @return the abstract image prototype
     */
    @Resource("watches.png")
    AbstractImagePrototype searchWatches();
    
    /**
     * Test connection.
     * 
     * @return the abstract image prototype
     */
    @Resource("link_go.png")
    AbstractImagePrototype test();
}
