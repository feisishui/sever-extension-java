define([
    "dojo/_base/declare",
    "esri/tasks/query"


], function(declare, Query){
    return declare("ClusterQuery", [Query], {
        constructor: function(map){
            this.map = map;
            this.clusterDistanceInPixels = 100;
            this.clusterField = null;
        },

        toJson:function(){
            var obj = this.inherited(arguments);
            obj.bbox = JSON.stringify(this.map.extent);
            obj.mapUnitsPerPixel = this.map.extent.getWidth()/this.map.width;
            obj.clusterDistanceInPixels = this.clusterDistanceInPixels;
            obj.clusterField = this.clusterField;

            return obj;
        }

    });



});