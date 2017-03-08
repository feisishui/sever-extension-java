define([
    "dojo/_base/declare",
    "dojo/_base/array",
    "dojo/number",
    "esri/Color",
    "dojo/_base/connect",
    "esri/renderers/ClassBreaksRenderer",

    "esri/SpatialReference",
    "esri/geometry/Point",
    "esri/graphic",
    "esri/symbols/SimpleMarkerSymbol",
    "esri/symbols/TextSymbol",
    "esri/symbols/SimpleLineSymbol",
    "esri/symbols/Font",

    "esri/dijit/PopupTemplate",
    "esri/layers/GraphicsLayer",
    "dojo/_base/lang",
    "esri/tasks/QueryTask",

    "layer/ClusterQuery"

], function (
    declare, arrayUtils, Number, Color, connect, ClassBreaksRenderer,
    SpatialReference, Point, Graphic, SimpleMarkerSymbol, TextSymbol, SimpleLineSymbol, Font,
    PopupTemplate, GraphicsLayer, lang, QueryTask, ClusterQuery
) {
    return declare([GraphicsLayer], {
        constructor: function(options) {
            this.url = options.url;
        },

        // override esri/layers/GraphicsLayer methods
        _setMap: function(map, surface) {
            this._saveMap = map;

            map.on("extent-change", lang.hitch(this, this.changeExtent));
            this.refreshLayer();
            // GraphicsLayer will add its own listener here
            var div = this.inherited(arguments);
            return div;
        },

        _unsetMap: function() {
            this.inherited(arguments);
            connect.disconnect(this._zoomEnd);
        },

        changeExtent:function(event){
            this.refreshLayer();
        },

        stringifyAllBreaks:function(breaks){
            var sBreaks = [];
            for (var i=0;i<breaks.length;i++){
                var sVal = this.getSuffixedNumber(breaks[i]);
                sBreaks.push(sVal);
            }
            return sBreaks;
        },
        getSuffixedNumber: function(value1) {

            //Decided to check if number is NaN or null and if so return dash
            if (value1 === null || isNaN(value1)){
                return "-";
            }

            var value = value1;
            if (value1 < 0){
                value = -value1;
            }



            var additionalPlaces = 0;

            var numPlaces = 100;

            if (value === 0) {
                value = "0";
            }else{
                if (value < 1000000000000000000) {
                    if (value >= 10000000000000000) { //10 quadrillion
                        value = Number.format(value1 / 1000000000000000, {
                                places: additionalPlaces + 0
                            }) + "Q"; //1 quadrillion
                    } else if (value >= 1000000000000000) { //1 quadrillion
                        value = Number.format(value1 / 1000000000000000, {
                                places: additionalPlaces + 0
                            }) + "Q"; //1 quadrillion
                    } else if (value >= 10000000000000) { //10 trillion
                        value = Number.format(value1 / 1000000000000, {
                                places: additionalPlaces + 0
                            }) + "T"; //1 trillion
                    } else if (value >= 1050000000000) { //1 trillion
                        value = Number.format(value1 / 1000000000000, {
                                places: additionalPlaces + 1
                            }) + "T"; //1 trillion
                    } else if (value >= 1000000000000) { //1 trillion
                        value = Number.format(value1 / 1000000000000, {
                                places: additionalPlaces + 0
                            }) + "T"; //1 trillion
                    } else if (value >= 10000000000) { //10 billion
                        value = Number.format(value1 / 1000000000, {
                                places: additionalPlaces + 0
                            }) + "B";
                    } else if (value >= 1050000000) { //1.05 billion
                        value = Number.format(value1 / 1000000000, {
                                places: additionalPlaces + 1
                            }) + "B";
                    } else if (value >= 1000000000) { //1 billion
                        value = Number.format(value1 / 1000000000, {
                                places: additionalPlaces + 0
                            }) + "B";
                    } else if (value >= 10000000) { //10 million
                        value = Number.format(value1 / 1000000, {
                                places: additionalPlaces + 0
                            }) + "M";
                    } else if (value >= 1050000) { //1.05 million
                        value = Number.format(value1 / 1000000, {
                                places: additionalPlaces + 1
                            }) + "M";
                    } else if (value >= 1000000) { //1 million
                        value = Number.format(value1 / 1000000, {
                                places: additionalPlaces + 0
                            }) + "M";
                    } else if (value >= 10000) { // 10K
                        value = Number.format(value1 / 1000, {
                                places: additionalPlaces + 0
                            }) + "K";
                    } else if (value >= 1050) {
                        value = Number.format(value1 / 1000, {
                                places: additionalPlaces + 1
                            }) + "K";
                    } else if (value >= 1000) {
                        value = Number.format(value1 / 1000, {
                                places: additionalPlaces + 0
                            }) + "K";
                    } else if (value >= 1 ) {
                        value = Number.format(value1, {
                            places: Math.min(additionalPlaces + 0, numPlaces)
                        });
                    } else if (value >= 0.1) {
                        value = Number.format(value1, {
                            places: Math.min(additionalPlaces + 1, numPlaces)
                        });
                    } else if (value >= 0.01) {
                        value = Number.format(value1, {
                            places: Math.min(additionalPlaces + 2, numPlaces)
                        });
                    } else if (value >= 0.001) {
                        value = Number.format(value1, {
                            places: Math.min(additionalPlaces + 3, numPlaces)
                        });
                    } else if (value >= 0.0001) {
                        value = Number.format(value1, {
                            places: Math.min(additionalPlaces + 4, numPlaces)
                        });
                    } else if (value >= 0.00001) {
                        value = Number.format(value1, {
                            places: Math.min(additionalPlaces + 5, numPlaces)
                        });
                    } else if (value >= 0.000001) {
                        value = Number.format(value1, {
                            places: Math.min(additionalPlaces + 6, numPlaces)
                        });
                    } else {
                        value = Number.format(value1);
                    }

                }
            }
            return value;
        },



        getAllInfos:function(breaks){
            var newBreaks = this.stringifyAllBreaks(breaks);
            var infoArray = [];
            for (var i=0;i<newBreaks.length-1;i++){
                var roundedMin = newBreaks[i];
                var roundedMax = newBreaks[i+1];
                var info = {
                    label: roundedMin + " to " + roundedMax,
                    minValue: this.parseSuffixedNumber(roundedMin),
                    maxValue: this.parseSuffixedNumber(roundedMax)
                };
                infoArray.push(info);
            }
            return infoArray;
        },

        parseSuffixedNumber:function(sNumber){
            var strippedNumber = sNumber.replace(/[B,Q,T,M,K]/gi, "");
            var num = Number.parse(strippedNumber);
            num *= this.parseFactor(sNumber);
            return num;
        },

        parseFactor:function(sNumber){
            var num = 1;
            if (this.endsWith(sNumber, "B")){
                num = 1000000000.0;
            }else if (this.endsWith(sNumber, "Q")){
                num = 1000000000000000.0;
            }else if (this.endsWith(sNumber, "T")){
                num = 1000000000.0;
            }else if (this.endsWith(sNumber, "M")){
                num = 1000000.0;
            }else if (this.endsWith(sNumber, "K")){
                num = 1000.0;
            }
            return num;
        },
        endsWith:function(str, suffix){
            return str && (str.indexOf(suffix, str.length-suffix.length) !== -1);
        },

        setTheRenderer:function(features){

            if (features.length < 1){
                return;
            }

            var fieldName = this.clusterField;

            features.sort(function(a,b){
                var aVal = a.attributes[fieldName];
                var bVal = b.attributes[fieldName];
                return aVal - bVal;
            });

            var breaks = [];

            if (features.length > 5){
                var inc = Math.floor(features.length/5);
                for (var j=0;j<4;j++){
                    var feature = features[inc*j];
                    var val = feature.attributes[fieldName];
                    breaks.push(val);
                    /*
                    if (j<4){
                        feature = features[inc*(j+1)];
                        val = feature.attributes[fieldName];
                        breaks.push(val);
                    }
                    */
                }

                breaks.push(features[features.length-1].attributes[fieldName]);

            }else if (features.length > 0){
                breaks.push(features[0].attributes[fieldName]);
                breaks.push(features[features.length-1].attributes[fieldName]);
            }else{
                breaks.push(0);
                breaks.push(1);
            }



            var white = new Color([255,255, 255, 0.7]);
            var ringColor = new Color([0, 163, 98, 0.7]);
            var symbols = [];
            symbols[0] = new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 28,
                new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, ringColor, 3),
                white);
            symbols[1] = new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 34,
                new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, ringColor, 5),
                white);
            symbols[2] = new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 40,
                new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, ringColor, 8),
                white);
            symbols[3] = new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 46,
                new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, ringColor, 10),
                white);
            symbols[4] = new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 52,
                new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, ringColor, 12),
                white);

            var renderer = new ClassBreaksRenderer(null, fieldName);

            var infos = this.getAllInfos(breaks);

            for (var k=0;k<infos.length;k++){
                var info = infos[k];
                info.symbol = symbols[k];
                renderer.addBreak(info);
            }

            this.setRenderer(renderer);
        },

        refreshLayer:function(){
            if (this._saveMap && this.clusterField){
                var fieldName = this.clusterField;
                this.clear();
                var queryTask = new QueryTask(this.url);
                var query = new ClusterQuery(this._saveMap);
                query.clusterDistanceInPixels = 100;
                query.outSpatialreference = this._saveMap.spatialReference;
                query.geometry = this._saveMap.extent;
                query.clusterField = fieldName;
                query.where = this.where;

                var deferredQueryTask = queryTask.execute(query);

                deferredQueryTask.then(
                    lang.hitch(this,
                        function(results){
                            var features = results.features;
                            this.setTheRenderer(features);



                            for (var i=0;i<features.length;i++){
                                var feature = features[i];

                                //var graphic = new Graphic(feature.geometry, null, feature.attributes);
                                this.add(feature);
                                var val = feature.attributes[fieldName];
                                var valueAndSuffix = this.getSuffixedNumber(val);
                                var label = new TextSymbol(valueAndSuffix)
                                    .setColor(new Color("#000"))
                                    .setFont(new Font("10pt", Font.STYLE_NORMAL, Font.VARIANT_NORMAL, Font.WEIGHT_NORMAL, "Arial"))
                                    .setOffset(0, -4);
                                //label.font.setSize("25pt");
                                //label.font.setFamily("arial");
                                //var font  = new Font();
                                //font.setSize("12pt");
                                //font.setWeight(Font.WEIGHT_BOLD);
                                //label.setFont(font);
                                this.add(new Graphic(feature.geometry, label, feature.attributes));
                            }




                        }
                    ),
                    lang.hitch(this,
                        function(error){

                        }
                    )
                )
            }



        }




    });
});
