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
  	var libertic = new OpenLayers.Layer.WMS(
            "libertic",
            wmsurl, 
            {
            	'layers': 'libertic', 
            	'format':format, 
            	'transparent':'true',
            	'tiled': !pureCoverage,
                'tilesOrigin' : map.maxExtent.left + ',' + map.maxExtent.bottom
            },
            {
            	'singleTile': true,
            	'ratio': 1.5,
            	'opacity': 1.0, 
            	'isBaseLayer': true,
            	'transitionEffect': 'resize',
            	'visibility': true
            }
    );
  	
  	// Add layer to map
  	map.addLayers([libertic]);
 	// build up all controls
    map.addControl(new OpenLayers.Control.PanZoomBar({
        position: new OpenLayers.Pixel(2, 15)
    }));
    map.addControl(new OpenLayers.Control.Navigation());
    map.zoomToExtent(bounds);
    
    // on click feature
	//declare Openlayer Click Control
	OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {               
        defaultHandlerOptions: {
            'single': true,
            'double': false,
            'pixelTolerance': 0,
            'stopSingle': false,
            'stopDouble': false
        },

        initialize: function(options) {
            this.handlerOptions = OpenLayers.Util.extend(
                {}, this.defaultHandlerOptions
            );
            OpenLayers.Control.prototype.initialize.apply(
                this, arguments
            ); 
            this.handler = new OpenLayers.Handler.Click(
                this, {
                    'click': this.trigger
                }, this.handlerOptions
            );
        }, 

        trigger: function(e) {
        	$("#card").html(loading);
        	var scale = map.getScale();
            var lonlat = map.getLonLatFromViewPortPx(e.xy).transform(new OpenLayers.Projection("EPSG:900913"), new OpenLayers.Projection("EPSG:4326"));
            $.ajax({
				type: 'GET',
				url: '/map/info?latitude=' +  lonlat.lat + '&longitude=' + lonlat.lon + '&scale=' + scale,
				async: 'false',
				context : $(this),
				dataType: 'html',
				success: function(data){
  					$("#card").html(data);
  				},
  				error: function(XMLHttpRequest, textStatus, errorThrown){
  					$("#card").html(errorThrown);
  				}
				
			}) 
        }
	})
    var click = new OpenLayers.Control.Click();
    map.addControl(click);
    click.activate();
  	
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

function getImageMap(){
	var wmsimgurl="/map/image?bbox=@@BBOX@@";
	var BBOX = map.getExtent().toBBOX();
	var url = wmsimgurl.replace("@@BBOX@@", BBOX);
	window.location=url;
	return url;
}
       