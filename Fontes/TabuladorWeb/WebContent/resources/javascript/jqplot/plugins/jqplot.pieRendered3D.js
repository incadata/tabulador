(function($) {
	//========My function begin@ashwni ==================
	
	ash(rad,color,data);
	
	//console.log("colors"+color.length);
	
	function ash(rad,color,data){
	
		var lastend = 0;
	
		var data=data;
		var myTotal = 0; // Automatically calculated so don't touch
	
	//	filling the array wid global variable window.mycol
	//	var myColor = [window.mycol[0],window.mycol[1],window.mycol[2],window.mycol[3],window.mycol[4],window.mycol[5],window.mycol[6],window.mycol[7],window.mycol[8],window.mycol[9],window.mycol[10],window.mycol[11],window.mycol[12],window.mycol[13],window.mycol[14],window.mycol[15]];
		var myColor=[];
		console.log(window.mycol.length);
		for(j=0;j<window.mycol.length;j++){
	
			myColor.push(window.mycol[j]);
	//		console.log(myColor[j]);
	
		} 
	//	console.log("data is"+color);
		ctx.scale(1,0.4);
	
		for(var e = 0; e <data.length; e++)
		{
	
	
			myTotal += data[e][1];
	
		}
	
	
		for (var j=0;j<window.n;j++){
	
			//	console.log("mydatalength"+data.length);
			for (var i = 0; i < data.length; i++) 
			{  ctx.beginPath();
			if (j == 0 ||j==1||j==2) {
	
	
				ctx.fillStyle = 'rgba(10,5,5,0.02)';
				// ctx.arc(0,0,rad,0,2*Math.PI,false);
			} else {
	
	
	
				ctx.fillStyle = (myColor[i]);
	
	
			};
	
	
	
	
	
			//console.log("window2"+window.mycol);
	
			ctx.moveTo(0,j);
	
			ctx.arc(0,j,rad,lastend,lastend+(Math.PI*2*(data[i][1]/myTotal)),false);
	
			ctx.lineTo(0,j);
	
			ctx.fill();
	
	
			lastend += Math.PI*2*(data[i][1]/myTotal);
	
	
	
			ctx.globalCompositeOperation="destination-over";
	
			}
		}
	
	
	
		//====================My function end============== 
	
		if (fill) {
			ctx.fill();
	
		}
		else {
			ctx.stroke();
	
		}       
	}


		// called with scope of series
		$.jqplot.PieRenderer3D.prototype.draw = function (ctx, gd, options, plot) {
			var i;
			var opts = (options != undefined) ? options : {};
			// offset and direction of offset due to legend placement
			var offx = 0;
			var offy = 0;
			var trans = 1;
	//		=========Declaring mycol as a Global variable@ashwni=============        
			window.mycol =this.seriesColors;
			//console.log("window1"+window.mycol);
	//		===================================================================       
			var colorGenerator = new $.jqplot.ColorGenerator(this.seriesColors);
			if (options.legendInfo && options.legendInfo.placement == 'insideGrid') {
				var li = options.legendInfo;
				switch (li.location) {
				case 'nw':
					offx = li.width + li.xoffset;
					break;
				case 'w':
					offx = li.width + li.xoffset;
					break;
				case 'sw':
					offx = li.width + li.xoffset;
					break;
				case 'ne':
					offx = li.width + li.xoffset;
					trans = -1;
					break;
				case 'e':
					offx = li.width + li.xoffset;
					trans = -1;
					break;
				case 'se':
					offx = li.width + li.xoffset;
					trans = -1;
					break;
				case 'n':
					offy = li.height + li.yoffset;
					break;
				case 's':
					offy = li.height + li.yoffset;
					trans = -1;
					break;
				default:
					break;
				}
			}
	
			var shadow = (opts.shadow != undefined) ? opts.shadow : this.shadow;
			var fill = (opts.fill != undefined) ? opts.fill : this.fill;
			var cw = ctx.canvas.width;
			var ch = ctx.canvas.height;
			var w = cw - offx - 2 * this.padding;
			var h = ch - offy - 2 * this.padding;
			var mindim = Math.min(w,h);
			var d = mindim;
	
			// Fixes issue #272.  Thanks hugwijst!
			// reset slice angles array.
			this._sliceAngles = [];
	
			var sm = this.sliceMargin;
			if (this.fill == false) {
				sm += this.lineWidth;
			}
	
			var rprime;
			var maxrprime = 0;
	
			var ang, ang1, ang2, shadowColor;
			var sa = this.startAngle / 180 * Math.PI;
	
			// have to pre-draw shadows, so loop throgh here and calculate some values also.
			for (var i=0, l=gd.length; i<l; i++) {
				ang1 = (i == 0) ? sa : gd[i-1][1] + sa;
				ang2 = gd[i][1] + sa;
	
				this._sliceAngles.push([ang1, ang2]);
	
				rprime = calcRPrime(ang1, ang2, this.sliceMargin, this.fill, this.lineWidth);
	
				if (Math.abs(ang2-ang1) > Math.PI) {
					maxrprime = Math.max(rprime, maxrprime);  
				}
			}
	
			if (this.diameter != null && this.diameter > 0) {
				this._diameter = this.diameter - 2*maxrprime;
			}
			else {
				this._diameter = d - 2*maxrprime;
			}
	
			// Need to check for undersized pie.  This can happen if
			// plot area too small and legend is too big.
			if (this._diameter < 6) {
				$.jqplot.log('Diameter of pie too small, not rendering.');
				return;
			}
	
			var r = this._radius = this._diameter/2;
	
			this._center = [(cw - trans * offx)/2 + trans * offx + maxrprime * Math.cos(sa), (ch - trans*offy)/2 + trans * offy + maxrprime * Math.sin(sa)];
	
			if (this.shadow) {
				for (var i=0, l=gd.length; i<l; i++) {
					shadowColor = 'rgba(0,0,0,'+this.shadowAlpha+')';
	
					this.renderer.drawSlice.call (this, ctx, this._sliceAngles[i][0], this._sliceAngles[i][1], shadowColor, true);
				}
			}
	
			for (var i=0; i<gd.length; i++) {
	
				this.renderer.drawSlice.call (this, ctx, this._sliceAngles[i][0], this._sliceAngles[i][1], colorGenerator.next(), false);
	
				if (this.showDataLabels && gd[i][2]*100 >= this.dataLabelThreshold) {
					var fstr, avgang = (this._sliceAngles[i][0] + this._sliceAngles[i][1])/2, label;
	
					if (this.dataLabels == 'label') {
						fstr = this.dataLabelFormatString || '%s';
						label = $.jqplot.sprintf(fstr, gd[i][0]);
					}
					else if (this.dataLabels == 'value') {
						fstr = this.dataLabelFormatString || '%d';
						label = $.jqplot.sprintf(fstr, this.data[i][1]);
					}
					else if (this.dataLabels == 'percent') {
						fstr = this.dataLabelFormatString || '%d%%';
						label = $.jqplot.sprintf(fstr, gd[i][2]*100);
					}
					else if (this.dataLabels.constructor == Array) {
						fstr = this.dataLabelFormatString || '%s';
						label = $.jqplot.sprintf(fstr, this.dataLabels[i]);
	
					}
	
					var fact = (this._radius ) * this.dataLabelPositionFactor + this.sliceMargin + this.dataLabelNudge;
	
					var x = this._center[0] + Math.cos(avgang) * fact + this.canvas._offsets.left;
					var y = this._center[1] + Math.sin(avgang) * fact + this.canvas._offsets.top;
	
					var labelelem = $('<div class="jqplot-pie-series jqplot-data-label" style="position:absolute;">' + label + '</div>').
					insertBefore(plot.eventCanvas._elem);
	
					if (this.dataLabelCenterOn) {
						x -= labelelem.width() / 2;
						y -= labelelem.height() / 2;
	
					}
					else {
						x -= labelelem.width() * Math.sin(avgang / 2);
						y -= labelelem.height() / 2;
					}
					x = Math.round(x);
					y = Math.round(y);
					labelelem.css({left:x ,top:y});
				}
			}            
		};
	
		$.jqplot.PieAxisRenderer3D = function() {
			$.jqplot.LinearAxisRenderer.call(this);
		};
	
		$.jqplot.PieAxisRenderer3D.prototype = new $.jqplot.LinearAxisRenderer();
		$.jqplot.PieAxisRenderer3D.prototype.constructor = $.jqplot.PieAxisRenderer3D;
	
	
		// There are no traditional axes on a pie chart.  We just need to provide
		// dummy objects with properties so the plot will render.
		// called with scope of axis object.
		$.jqplot.PieAxisRenderer3D.prototype.init = function(options){
			//
			this.tickRenderer = $.jqplot.PieTickRenderer;
			$.extend(true, this, options);
			// I don't think I'm going to need _dataBounds here.
			// have to go Axis scaling in a way to fit chart onto plot area
			// and provide u2p and p2u functionality for mouse cursor, etc.
			// for convienence set _dataBounds to 0 and 100 and
			// set min/max to 0 and 100.
			this._dataBounds = {min:0, max:100};
			this.min = 0;
			this.max = 100;
			this.showTicks = false;
			this.ticks = [];
			this.showMark = false;
			this.show = false; 
		};
	
	
	
	
		$.jqplot.PieLegendRenderer3D = function(){
			$.jqplot.TableLegendRenderer.call(this);
		};
	
		$.jqplot.PieLegendRenderer3D.prototype = new $.jqplot.TableLegendRenderer();
		$.jqplot.PieLegendRenderer3D.prototype.constructor = $.jqplot.PieLegendRenderer3D;
	
		/**
		 * Class: $.jqplot.PieLegendRenderer
		 * Legend Renderer specific to pie plots.  Set by default
		 * when user creates a pie plot.
		 */
		$.jqplot.PieLegendRenderer3D.prototype.init = function(options) {
			// Group: Properties
			//
			// prop: numberRows
			// Maximum number of rows in the legend.  0 or null for unlimited.
			this.numberRows = null;
			// prop: numberColumns
			// Maximum number of columns in the legend.  0 or null for unlimited.
			this.numberColumns = null;
			$.extend(true, this, options);
		};
	
		// called with context of legend
		$.jqplot.PieLegendRenderer3D.prototype.draw = function() {
			var legend = this;
			if (this.show) {
				var series = this._series;
	
	
				this._elem = $(document.createElement('table'));
				this._elem.addClass('jqplot-table-legend');
	
				var ss = {position:'absolute'};
				if (this.background) {
					ss['background'] = this.background;
				}
				if (this.border) {
					ss['border'] = this.border;
				}
				if (this.fontSize) {
					ss['fontSize'] = this.fontSize;
				}
				if (this.fontFamily) {
					ss['fontFamily'] = this.fontFamily;
				}
				if (this.textColor) {
					ss['textColor'] = this.textColor;
				}
				if (this.marginTop != null) {
					ss['marginTop'] = this.marginTop;
				}
				if (this.marginBottom != null) {
					ss['marginBottom'] = this.marginBottom;
				}
				if (this.marginLeft != null) {
					ss['marginLeft'] = this.marginLeft;
				}
				if (this.marginRight != null) {
					ss['marginRight'] = this.marginRight;
				}
	
				this._elem.css(ss);
	
				// Pie charts legends don't go by number of series, but by number of data points
				// in the series.  Refactor things here for that.
	
				var pad = false, 
				reverse = false,
				nr, 
				nc;
				var s = series[0];
	
				var colorGenerator = new $.jqplot.ColorGenerator(s.seriesColors);
	
				if (s.show) {
					var pd = s.data;
					if (this.numberRows) {
						nr = this.numberRows;
						if (!this.numberColumns){
							nc = Math.ceil(pd.length/nr);
						}
						else{
							nc = this.numberColumns;
						}
					}
					else if (this.numberColumns) {
						nc = this.numberColumns;
						nr = Math.ceil(pd.length/this.numberColumns);
					}
					else {
						nr = pd.length;
						nc = 1;
					}
	
					var i, j;
					var tr, td1, td2; 
					var lt, rs, color;
					var idx = 0; 
					var div0, div1;   
	
					for (i=0; i<nr; i++) {
						tr = $(document.createElement('tr'));
						tr.addClass('jqplot-table-legend');
	
						if (reverse){
							tr.prependTo(this._elem);
						}
	
						else{
							tr.appendTo(this._elem);
						}
	
						for (j=0; j<nc; j++) {
							if (idx < pd.length){
								lt = this.labels[idx] || pd[idx][0].toString();
								color = colorGenerator.next();
								if (!reverse){
									if (i>0){
										pad = true;
									}
									else{
										pad = false;
									}
								}
								else{
									if (i == nr -1){
										pad = false;
									}
									else{
										pad = true;
									}
								}
								rs = (pad) ? this.rowSpacing : '0';
	
	
								td1 = $(document.createElement('td'));
								td1.addClass('jqplot-table-legend jqplot-table-legend-swatch');
								td1.css({textAlign: 'center', paddingTop: rs});
	
								div0 = $(document.createElement('div'));
								div0.addClass('jqplot-table-legend-swatch-outline');
								div1 = $(document.createElement('div'));
								div1.addClass('jqplot-table-legend-swatch');
								div1.css({backgroundColor: color, borderColor: color});
								td1.append(div0.append(div1));
	
								td2 = $(document.createElement('td'));
								td2.addClass('jqplot-table-legend jqplot-table-legend-label');
								td2.css('paddingTop', rs);
	
								if (this.escapeHtml){
									td2.text(lt);
								}
								else {
									td2.html(lt);
								}
								if (reverse) {
									td2.prependTo(tr);
									td1.prependTo(tr);
								}
								else {
									td1.appendTo(tr);
									td2.appendTo(tr);
								}
								pad = true;
							}
							idx++;
						}   
					}
				}
			}
			return this._elem;                
		};
	
		$.jqplot.PieRenderer3D.prototype.handleMove = function(ev, gridpos, datapos, neighbor, plot) {
			if (neighbor) {
				var ins = [neighbor.seriesIndex, neighbor.pointIndex, neighbor.data];
				plot.target.trigger('jqplotDataMouseOver', ins);
				if (plot.series[ins[0]].highlightMouseOver && !(ins[0] == plot.plugins.PieRenderer3D.highlightedSeriesIndex && ins[1] == plot.series[ins[0]]._highlightedPoint)) {
					plot.target.trigger('jqplotDataHighlight', ins);
					highlight (plot, ins[0], ins[1]);
				}
			}
			else if (neighbor == null) {
				unhighlight (plot);
			}
		};
	
	
		// this.eventCanvas._elem.bind($.jqplot.eventListenerHooks[i][0], {plot:this}, $.jqplot.eventListenerHooks[i][1]);
	
		// setup default renderers for axes and legend so user doesn't have to
		// called with scope of plot
		function preInit(target, data, options) {
			options = options || {};
			options.axesDefaults = options.axesDefaults || {};
			options.legend = options.legend || {};
			options.seriesDefaults = options.seriesDefaults || {};
			// only set these if there is a pie series
			var setopts = false;
			if (options.seriesDefaults.renderer == $.jqplot.PieRenderer3D) {
				setopts = true;
			}
			else if (options.series) {
				for (var i=0; i < options.series.length; i++) {
					if (options.series[i].renderer == $.jqplot.PieRenderer3D) {
						setopts = true;
					}
				}
			}
	
			if (setopts) {
				options.axesDefaults.renderer = $.jqplot.PieAxisRenderer3D;
				options.legend.renderer = $.jqplot.PieLegendRenderer3D;
				options.legend.preDraw = true;
				options.seriesDefaults.pointLabels = {show: false};
			}
		}
	
		function postInit(target, data, options) {
			for (var i=0; i<this.series.length; i++) {
				if (this.series[i].renderer.constructor == $.jqplot.PieRenderer3D) {
					// don't allow mouseover and mousedown at same time.
					if (this.series[i].highlightMouseOver) {
						this.series[i].highlightMouseDown = false;
					}
				}
			}
		}
	
		// called with scope of plot
		function postParseOptions(options) {
			for (var i=0; i<this.series.length; i++) {
				this.series[i].seriesColors = this.seriesColors;
	
				this.series[i].colorGenerator = $.jqplot.colorGenerator;
			}
		}
	
		function highlight (plot, sidx, pidx) {
			var s = plot.series[sidx];
			var canvas = plot.plugins.PieRenderer3D.highlightCanvas;
			canvas._ctx.clearRect(0,0,canvas._ctx.canvas.width, canvas._ctx.canvas.height);
			s._highlightedPoint = pidx;
			plot.plugins.PieRenderer3D.highlightedSeriesIndex = sidx;
	
			s.renderer.drawSlice.call(data,s, canvas._ctx, s._sliceAngles[pidx][0], s._sliceAngles[pidx][1], s.highlightColorGenerator.get(pidx), false);
		}
	
		function unhighlight (plot) {
			var canvas = plot.plugins.PieRenderer3D.highlightCanvas;
			canvas._ctx.clearRect(0,0, canvas._ctx.canvas.width, canvas._ctx.canvas.height);
			for (var i=0; i<plot.series.length; i++) {
				plot.series[i]._highlightedPoint = null;
			}
			plot.plugins.PieRenderer3D.highlightedSeriesIndex = null;
			plot.target.trigger('jqplotDataUnhighlight');
		}
	
		function handleMove(ev, gridpos, datapos, neighbor, plot) {
			if (neighbor) {
				var ins = [neighbor.seriesIndex, neighbor.pointIndex, neighbor.data];
				var evt1 = jQuery.Event('jqplotDataMouseOver');
				evt1.pageX = ev.pageX;
				evt1.pageY = ev.pageY;
				plot.target.trigger(evt1, ins);
				if (plot.series[ins[0]].highlightMouseOver && !(ins[0] == plot.plugins.PieRenderer3D.highlightedSeriesIndex && ins[1] == plot.series[ins[0]]._highlightedPoint)) {
					var evt = jQuery.Event('jqplotDataHighlight');
					evt.which = ev.which;
					evt.pageX = ev.pageX;
					evt.pageY = ev.pageY;
					plot.target.trigger(evt, ins);
					highlight (plot, ins[0], ins[1]);
				}
			}
			else if (neighbor == null) {
				unhighlight (plot);
			}
		} 
	
		function handleMouseDown(ev, gridpos, datapos, neighbor, plot) {
			if (neighbor) {
				var ins = [neighbor.seriesIndex, neighbor.pointIndex, neighbor.data];
				if (plot.series[ins[0]].highlightMouseDown && !(ins[0] == plot.plugins.PieRenderer3D.highlightedSeriesIndex && ins[1] == plot.series[ins[0]]._highlightedPoint)) {
					var evt = jQuery.Event('jqplotDataHighlight');
					evt.which = ev.which;
					evt.pageX = ev.pageX;
					evt.pageY = ev.pageY;
					plot.target.trigger(evt, ins);
					highlight (plot, ins[0], ins[1]);
				}
			}
			else if (neighbor == null) {
				unhighlight (plot);
			}
		}
	
		function handleMouseUp(ev, gridpos, datapos, neighbor, plot) {
			var idx = plot.plugins.PieRenderer3D.highlightedSeriesIndex;
			if (idx != null && plot.series[idx].highlightMouseDown) {
				unhighlight(plot);
			}
		}
	
		function handleClick(ev, gridpos, datapos, neighbor, plot) {
			if (neighbor) {
				var ins = [neighbor.seriesIndex, neighbor.pointIndex, neighbor.data];
				var evt = jQuery.Event('jqplotDataClick');
				evt.which = ev.which;
				evt.pageX = ev.pageX;
				evt.pageY = ev.pageY;
				plot.target.trigger(evt, ins);
			}
		}
	
		function handleRightClick(ev, gridpos, datapos, neighbor, plot) {
			if (neighbor) {
				var ins = [neighbor.seriesIndex, neighbor.pointIndex, neighbor.data];
				var idx = plot.plugins.PieRenderer3D.highlightedSeriesIndex;
				if (idx != null && plot.series[idx].highlightMouseDown) {
					unhighlight(plot);
				}
				var evt = jQuery.Event('jqplotDataRightClick');
				evt.which = ev.which;
				evt.pageX = ev.pageX;
				evt.pageY = ev.pageY;
				plot.target.trigger(evt, ins);
			}
		}    
	
		// called within context of plot
		// create a canvas which we can draw on.
		// insert it before the eventCanvas, so eventCanvas will still capture events.
		function postPlotDraw() {
			// Memory Leaks patch    
			if (this.plugins.PieRenderer3D && this.plugins.PieRenderer3D.highlightCanvas) {
				this.plugins.PieRenderer3D.highlightCanvas.resetCanvas();
				this.plugins.PieRenderer3D.highlightCanvas = null;
			}
	
			this.plugins.PieRenderer3D = {highlightedSeriesIndex:null};
			this.plugins.PieRenderer3D.highlightCanvas = new $.jqplot.GenericCanvas();
	
			// do we have any data labels?  if so, put highlight canvas before those
			var labels = $(this.targetId+' .jqplot-data-label');
	
			if (labels.length) {
				$(labels[0]).before(this.plugins.PieRenderer3D.highlightCanvas.createElement(this._gridPadding, 'jqplot-PieRenderer3D-highlight-canvas', this._plotDimensions, this));
			}
			// else put highlight canvas before event canvas.
			else {
				this.eventCanvas._elem.before(this.plugins.PieRenderer3D.highlightCanvas.createElement(this._gridPadding, 'jqplot-PieRenderer3D-highlight-canvas', this._plotDimensions, this));
			}
	
			var hctx = this.plugins.PieRenderer3D.highlightCanvas.setContext();
			this.eventCanvas._elem.bind('mouseleave', {plot:this}, function (ev) { unhighlight(ev.data.plot); });
		}
	
		$.jqplot.preInitHooks.push(preInit);
	
		$.jqplot.PieTickRenderer3D = function() {
			$.jqplot.AxisTickRenderer3D.call(this);
		};
	
		$.jqplot.PieTickRenderer3D.prototype = new $.jqplot.AxisTickRenderer();
		$.jqplot.PieTickRenderer3D.prototype.constructor = $.jqplot.PieTickRenderer;
	
	})(jQuery);