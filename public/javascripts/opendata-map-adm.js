var map;
var pureCoverage = false;
var format = 'image/png';
// pink tile avoidance
OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;
// make OL compute scale according to WMS spec
OpenLayers.DOTS_PER_INCH = 25.4 / 0.28;

var DeleteFeature = OpenLayers.Class(OpenLayers.Control, {
    initialize: function(layer, options) {
        OpenLayers.Control.prototype.initialize.apply(this, [options]);
        this.layer = layer;
        this.handler = new OpenLayers.Handler.Feature(
            this, layer, {click: this.clickFeature}
        );
    },
    clickFeature: function(feature) {
        // if feature doesn't have a fid, destroy it
        if(feature.fid == undefined) {
            this.layer.destroyFeatures([feature]);
        } else {
            feature.state = OpenLayers.State.DELETE;
            this.layer.events.triggerEvent("afterfeaturemodified", 
                                           {feature: feature});
            feature.renderIntent = "select";
            this.layer.drawFeature(feature);
        }
    },
    setMap: function(map) {
        this.handler.setMap(map);
        OpenLayers.Control.prototype.setMap.apply(this, arguments);
    },
    CLASS_NAME: "OpenLayers.Control.DeleteFeature"
});

function init(wmsurl, wfsurl, loading) {
	
	var bounds = new OpenLayers.Bounds(
            -20037484.241, -85541385.529,
            20037508.343, 18428629.146
	);

    var options = {
            controls: [],
            maxExtent: bounds,
            maxResolution: 406132.86982421874,
    		minScale: 5000000,
            maxScale: 200000000,
            projection: new OpenLayers.Projection("EPSG:900913"),
            units: 'm'
        };
    map = new OpenLayers.Map('map', options);
    
    var countries = new OpenLayers.Layer.WMS(
            "Countries",
            wmsurl, 
            {
            	'layers': 'libertic:countries', 
            	'format':format, 
            	'transparent':'true',
            	'tiled': !pureCoverage,
                'tilesOrigin' : map.maxExtent.left + ',' + map.maxExtent.bottom
            },
            {
            	'singleTile': true,
            	'ratio': 1,
            	'opacity': 1.0, 
            	'isBaseLayer': true, 
            	'visibility': true
            }
    );

    var saveStrategy = new OpenLayers.Strategy.Save();
    
    var wfs = new OpenLayers.Layer.Vector("Editable Features", {
        strategies: [new OpenLayers.Strategy.BBOX(), saveStrategy],
        projection: new OpenLayers.Projection("EPSG:900913"),
        protocol: new OpenLayers.Protocol.WFS({
            version: "1.0.0",
            srsName: "EPSG:900913",
            url: wfsurl,
            featureNS :  "http://www.libertic.org",
            featureType: "libertic_city_ok",
            geometryName: "the_geom",
            schema: wfsurl + "/DescribeFeatureType?version=1.1.0&typename=libertic:libertic_city_ok"
        })
    }); 
   
    map.addLayers([countries, wfs]);

    var panel = new OpenLayers.Control.Panel({
        displayClass: 'customEditingToolbar',
        allowDepress: true
    });
    
    var draw = new OpenLayers.Control.DrawFeature(
        wfs, OpenLayers.Handler.Polygon,
        {
            title: "Draw Feature",
            displayClass: "olControlDrawFeaturePolygon",
            multi: true
        }
    );
    
    var edit = new OpenLayers.Control.ModifyFeature(wfs, {
        title: "Modify Feature",
        displayClass: "olControlModifyFeature"
    });

    var del = new DeleteFeature(wfs, {title: "Delete Feature"});
   
    var save = new OpenLayers.Control.Button({
        title: "Save Changes",
        trigger: function() {
            if(edit.feature) {
                edit.selectControl.unselectAll();
            }
            saveStrategy.save();
        },
        displayClass: "olControlSaveFeatures"
    });

    panel.addControls([save, del, edit, draw]);
    map.addControl(panel);
    // build up all controls
    map.addControl(new OpenLayers.Control.PanZoomBar({
        position: new OpenLayers.Pixel(2, 15)
    }));
    map.addControl(new OpenLayers.Control.Navigation());
    map.zoomToExtent(bounds);
    
 // map position
    map.setCenter(new OpenLayers.LonLat(1.2, 46.8).transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913")), 12);
    if (navigator.geolocation)
    {
      navigator.geolocation.getCurrentPosition(function(position)
      {
    	  map.setCenter(new OpenLayers.LonLat(position.coords.longitude,  position.coords.latitude).transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913")), 12);
      });
    }
}


       