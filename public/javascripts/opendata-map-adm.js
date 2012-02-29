var map;
var pureCoverage = false;
var format = 'image/png';
// pink tile avoidance
OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;
// make OL compute scale according to WMS spec
OpenLayers.DOTS_PER_INCH = 25.4 / 0.28;

function init(wmsurl, loading) {
	// Map is in mercator this time, so over-ride the default
	// options that assume lat/lon.
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
  	
	// Create the map object
  	map = new OpenLayers.Map('map', options);
  	var countries = new OpenLayers.Layer.WMS(
            "Countries",
            wmsurl, 
            {
            	'layers': 'libertic:country', 
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
  	
  	// create WFS point layer
  	var wfslayer = new OpenLayers.Layer.WFS( "couche geoserver",
  			wfsurl,
            {typename: "libertic:libertic_city_ok", maxfeatures: 10},
            { featureClass: OpenLayers.Feature.WFS});
  	

  	// Add layer to map
  	map.addLayers([countries,  wfslayer]);
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
       