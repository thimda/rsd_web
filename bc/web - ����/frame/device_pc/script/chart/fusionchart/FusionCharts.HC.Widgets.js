/*
 FusionCharts JavaScript Library
 Copyright FusionCharts Technologies LLP
 License Information at <http://www.fusioncharts.com/license>

 @author FusionCharts Technologies LLP
 @version fusioncharts/3.2.3-beta.4335
*/
(function() {
    function X(d, c, a) {
        var b = z(c) !== void 0;
        this.paletteId = d;
        this.themeColor = c;
        this.isTheme = b;
        this._iterator = 0;
        this.defaultGaugePaletteOptions = a && a.defaultGaugePaletteOptions || Y
    }
    function qa() {}
    function ha(d, c, a, b) {
        c = f(c, b);
        d = f(d, a);
        return ! c || !d ? 1 : d / a == c / b ? a / d: Math.min(a / d, b / c)
    }
    function da(d, c, a) {
        var b, k, e, g, h, l, j;
        a || (a = Y.paletteColors[0]);
        if (d && d.length > 0) {
            for (e = d.length - 1; e >= 0; e -= 1) if (b = d[e]) if (b.minvalue = Number(b.minvalue), b.maxvalue = Number(b.maxvalue), b) b.minvalue > b.maxvalue ? (k = b.minvalue, b.minvalue = b.maxvalue, b.maxvalue = k) : b.minvalue < b.maxvalue || d.splice(e, 1);
            d.sort(Oa);
            if (!d[0].code) d[0].code = a[0];
            if (z(d[0].alpha) == void 0) d[0].alpha = V;
            e = 0;
            for (g = d.length - 1; e < g; e += 1) {
                h = e + 1;
                b = d[e];
                k = d[h];
                if (!k.code) k.code = a[h];
                if (z(k.alpha) == void 0) k.alpha = V;
                if (b.maxvalue > k.minvalue) {
                    if (b.maxvalue > k.maxvalue) {
                        h = T(b);
                        h.maxvalue = b.maxvalue;
                        j = h.minvalue = k.maxvalue;
                        for (l = e + 2; l < g && d[l].minvalue < j; l += 1);
                        d.splice(l, 0, h);
                        g += 1
                    }
                    b.maxvalue = k.minvalue
                }
            }
        }
        if (! (d && d.length > 0)) c || (c = {
            code: "000000",
            alpha: "100",
            bordercolor: "000000",
            borderalpha: "100"
        }),
        d = [c],
        this.defaultAsigned = !0;
        this.colorArr = d
    }
    var na = FusionCharts(["private", "modules.renderer.highcharts-widgets"]);
    if (na !== void 0) {
        var y = na.hcLib,
        J = y.BLANKSTRING,
        ia = y.createTrendLine,
        m = y.pluck,
        z = y.getValidValue,
        f = y.pluckNumber,
        Pa = y.defaultPaletteOptions,
        Aa = y.getFirstValue,
        K = y.FC_CONFIG_STRING,
        T = y.extend2,
        Ba = y.getDashStyle,
        Ca = y.hasSVG,
        Qa = y.getFirstColor,
        L = y.graphics.getDarkColor,
        R = y.graphics.getLightColor,
        G = y.graphics.convertColor,
        aa = y.graphics.parseColor,
        O = y.COLOR_TRANSPARENT,
        x = y.chartAPI,
        fa = y.titleSpaceManager,
        Ra = y.axisMinMaxSetter,
        Da = y.STRINGUNDEFINED,
        Sa = y.graphics.mapSymbolName,
        ja = x.singleseries,
        q = y.COMMASTRING,
        ga = y.ZEROSTRING,
        la = y.ONESTRING,
        Z = y.parseUnsafeString,
        Q = window,
        Ta = /msie/i.test(navigator.userAgent) && !Q.opera,
        Q = y.Highcharts,
        S = document,
        Ua = S.documentMode === 8,
        Ga = Math,
        Ea = Ga.min,
        ka = Ga.PI / 180,
        ra = y.regex.dropHash,
        Ha = y.toPrecision,
        sa = y.HASHSTRING,
        Va = y.getColorCodeString,
        va = y.regex.hexcode,
        N = Q.seriesTypes,
        ta = Q.attr,
        Ia = S.documentElement.ontouchstart !== void 0,
        oa = Q.each,
        S = Q.merge,
        ua = Q.Color,
        W = function(d) {
            return typeof d === "object"
        },
        ea = function(d) {
            return typeof d === "string"
        },
        U = function(d) {
            return d !== void 0 && d !== null
        },
        Ja = "rgba(192,192,192," + (Ca ? 1.0E-6: 0.0020) + ")",
        ba = Ta && !Ca ? "visible": "",
        wa = y.regex.startsRGBA,
        Ka = y.POSITION_RIGHT,
        La = y.POSITION_LEFT,
        V = y.HUNDREDSTRING,
        Wa = y.BGRATIOSTRING,
        Xa = y.COMMASPACE,
        pa = function(d, c, a, b) {
            var a = d[K].smartLabel,
            k = d.series[0],
            e,
            g = 0,
            h = 0,
            l,
            j,
            i;
            e = d.xAxis.labels.style;
            d = e.lineHeight.split(/px/)[0];
            a.setStyle(e);
            i = k.majorTM.length;
            for (e = 0; e < i; e++) j = k.majorTM[e],
            l = j.displayValue,
            l = a.getSmartText(l, c * 0.7, d),
            h = Math.max(h, b ? l.height: l.width),
            j.displayValue = l.text;
            h += k.tickValueDistance;
            k.showTickMarks && (g = Math.max(k.majorTMHeight, k.minorTMHeight) + k.tickMarkDistance);
            return g + h
        },
        xa = function(d, c, a, b, k, e, g) {
            var h = e.options,
            l = h.minorTM,
            j = h.min,
            i = b / (h.max - j),
            f = h.majorTM,
            o,
            n,
            C = h.ticksOnRight == 1,
            m = C == 1 ? La: Ka,
            s = c,
            u = h.minorTMHeight;
            n = h.majorTMHeight;
            o = h.tickValueDistance;
            var w = h.minorTMColor,
            r = h.minorTMAlpha,
            t = h.minorTMThickness,
            A = h.majorTMColor,
            P = h.majorTMAlpha,
            D = h.majorTMThickness,
            I = h.tickMarkDistance,
            F = e.dataLabelsGroup,
            E = h.tickValueStyle,
            B = parseInt(E.lineHeight),
            q = Math.max(n, u),
            H = 0,
            z = h.showTickMarks == 1;
            g || (s = b + c, i = -i);
            C ? (d = d + I + a, a = d + u, n = d + n, o = d + q + o, H = 1) : (d = d - I - 1, a = d - u - 1, n = d - n - 1, o = d - (q + o) - 1);
            h.connectTickMarks && z && (q = k.crispLine(["M", d, c + 1, "L", d, c + b], D), k.path(q).attr({
                stroke: G(A, P),
                "stroke-linecap": "round",
                "stroke-width": D
            }).add(F));
            if (!e.minorTM) e.minorTM = [];
            if (u != 0 && z) {
                c = 0;
                for (b = l.length; c < b; c += 1) h = l[c],
                h = s + (h - j) * i,
                q = k.crispLine(["M", d + H, h, "L", a, h], t),
                e.minorTM[c] = k.path(q).attr({
                    stroke: G(w, r),
                    "stroke-linecap": "round",
                    "stroke-width": t
                }).add(F)
            }
            if (!e.majorTM) e.majorTM = [];
            c = 0;
            for (b = f.length; c < b; c += 1) l = f[c],
            h = l.value,
            l = l.displayValue,
            h = s + (h - j) * i,
            u != 0 && z && (q = k.crispLine(["M", d + H, h, "L", n, h], t), e.majorTM[c] = k.path(q).attr({
                stroke: G(A, P),
                "stroke-linecap": "round",
                "stroke-width": D
            }).add(F)),
            e.majorTM[c] = k.text(l, o, h + B * 0.35).attr({
                align: m
            }).css(E).add(F)
        },
        Ma = function(d, c, a, b, k, e, g) {
            var h = e.options,
            l = h.minorTM,
            j = h.min,
            i = a / (h.max - j),
            f = h.majorTM,
            o = h.ticksBelowGauge == 1,
            n = d,
            m = d,
            v = e.dataLabelsGroup,
            n = h.minorTMHeight,
            s = h.majorTMHeight,
            u = h.tickValueDistance,
            w = h.minorTMColor,
            r = h.minorTMAlpha,
            t = h.minorTMThickness,
            A = h.majorTMColor,
            P = h.majorTMAlpha,
            D = h.majorTMThickness,
            I = h.tickValueStyle,
            F = I.lineHeight.split(/px/)[0] * 1,
            E = Math.max(s, n),
            B = 0,
            q = h.showTickMarks == 1;
            g && (i = -i, m = d + a);
            o ? (c += h.tickMarkDistance, g = c + n, b = c + s, u = c + u + F + E, B = 1) : (c = c - b - h.tickMarkDistance, g = c - n, b = c - s, u = c - u - F - E + F * 0.75);
            h.connectTickMarks && q && (h = k.crispLine(["M", d + 1, c, "L", d + a, c], D), k.path(h).attr({
                stroke: G(A, P),
                "stroke-linecap": "round",
                "stroke-width": D
            }).add(v));
            if (!e.minorTM) e.minorTM = [];
            if (n != 0 && q) {
                d = 0;
                for (a = l.length; d < a; d += 1) h = l[d],
                n = m + (h - j) * i,
                h = k.crispLine(["M", n, c + B, "L", n, g], t),
                e.minorTM[d] = k.path(h).attr({
                    stroke: G(w, r),
                    "stroke-linecap": "round",
                    "stroke-width": t
                }).add(v)
            }
            if (!e.majorTM) e.majorTM = [];
            if (!e.tmLabel) e.tmLabel = [];
            d = 0;
            for (a = f.length; d < a; d += 1) l = f[d],
            h = l.value,
            l = l.displayValue,
            n = m + (h - j) * i,
            s != 0 && q && (h = k.crispLine(["M", n, c + B, "L", n, b], D), e.majorTM[d] = k.path(h).attr({
                stroke: G(A, P),
                "stroke-linecap": "round",
                "stroke-width": D
            }).add(v)),
            e.tmLabel[d] = k.text(l, n, u).attr({
                align: "center"
            }).css(I).add(v)
        },
        Y = {
            paletteColors: [["8BBA00", "F6BD0F", "FF654F", "AFD8F8", "FDB398", "CDC309", "B1D0D2", "FAD1B9", "B8A79E", "D7CEA5", "C4B3CE", "E9D3BE", "EFE9AD", "CEA7A2", "B2D9BA"], ["8BBA00", "F6BD0F", "FF654F", "AFD8F8", "FDB398", "CDC309", "B1D0D2", "FAD1B9", "B8A79E", "D7CEA5", "C4B3CE", "E9D3BE", "EFE9AD", "CEA7A2", "B2D9BA"], ["8BBA00", "F6BD0F", "FF654F", "AFD8F8", "FDB398", "CDC309", "B1D0D2", "FAD1B9", "B8A79E", "D7CEA5", "C4B3CE", "E9D3BE", "EFE9AD", "CEA7A2", "B2D9BA"], ["8BBA00", "F6BD0F", "FF654F", "AFD8F8", "FDB398", "CDC309", "B1D0D2", "FAD1B9", "B8A79E", "D7CEA5", "C4B3CE", "E9D3BE", "EFE9AD", "CEA7A2", "B2D9BA"], ["8BBA00", "F6BD0F", "FF654F", "AFD8F8", "FDB398", "CDC309", "B1D0D2", "FAD1B9", "B8A79E", "D7CEA5", "C4B3CE", "E9D3BE", "EFE9AD", "CEA7A2", "B2D9BA"]],
            bgColor: ["CBCBCB,E9E9E9", "CFD4BE,F3F5DD", "C5DADD,EDFBFE", "A86402,FDC16D", "FF7CA0,FFD1DD"],
            bgAngle: [270, 270, 270, 270, 270],
            bgRatio: ["0,100", "0,100", "0,100", "0,100", "0,100"],
            bgAlpha: ["50,50", "60,50", "40,20", "20,10", "30,30"],
            toolTipBgColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
            toolTipBorderColor: ["545454", "545454", "415D6F", "845001", "68001B"],
            baseFontColor: ["555555", "60634E", "025B6A", "A15E01", "68001B"],
            tickColor: ["333333", "60634E", "025B6A", "A15E01", "68001B"],
            trendDarkColor: ["333333", "60634E", "025B6A", "A15E01", "68001B"],
            trendLightColor: ["f1f1f1", "F3F5DD", "EDFBFE", "FFF5E8", "FFD1DD"],
            pointerBorderColor: ["545454", "60634E", "415D6F", "845001", "68001B"],
            pointerBgColor: ["545454", "60634E", "415D6F", "845001", "68001B"],
            canvasBgColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
            canvasBgAngle: [0, 0, 0, 0, 0],
            canvasBgAlpha: ["100", "100", "100", "100", "100"],
            canvasBgRatio: ["", "", "", "", ""],
            canvasBorderColor: ["545454", "545454", "415D6F", "845001", "68001B"],
            canvasBorderAlpha: [100, 100, 100, 90, 100],
            altHGridColor: ["EEEEEE", "D8DCC5", "99C4CD", "DEC49C", "FEC1D0"],
            altHGridAlpha: [50, 35, 10, 20, 15],
            altVGridColor: ["767575", "D8DCC5", "99C4CD", "DEC49C", "FEC1D0"],
            altVGridAlpha: [10, 20, 10, 15, 10],
            borderColor: ["767575", "545454", "415D6F", "845001", "68001B"],
            borderAlpha: [50, 50, 50, 50, 50],
            legendBgColor: ["ffffff", "ffffff", "ffffff", "ffffff", "ffffff"],
            legendBorderColor: ["545454", "545454", "415D6F", "845001", "D55979"],
            plotFillColor: ["767575", "D8DCC5", "99C4CD", "DEC49C", "FEC1D0"],
            plotBorderColor: ["999999", "8A8A8A", "6BA9B6", "C1934D", "FC819F"],
            msgLogColor: ["717170", "7B7D6D", "92CDD6", "965B01", "68001B"],
            TrendLightShadeOffset: 30
        };
        na.addEventListener("internal.hc.rendered",
        function(d, c) {
            var a = d.sender,
            b = a.__state,
            k = c.hcInstance.options.series[0] || {},
            e = f(k.updateInterval, k.refreshInterval) * 1E3,
            g = k.dataStreamURL,
            k = c.realtimeEnabled && e && g !== "undefined" && c.hcInstance.options.series[0],
            h = function() {
                g && b._rtAjaxObj.get(g)
            };
            e < 10 && (e = 10);
            clearTimeout(b._toRealtime);
            b._rtAjaxObj && b._rtAjaxObj.abort();
            if (k) b._rtAjaxObj = new na.ajax(function(k, g, i, f) {
                g = c.linearDataParser(k);
                g.interval = e;
                a.isActive() ? (c.hcInstance && c.hcInstance.series && c.hcInstance.series[0] && c.hcInstance.series[0].realtimeUpdate(g), b._toRealtime = setTimeout(h, e)) : clearTimeout(b._toRealtime);
                na.raiseEvent("RealTimeUpdateComplete", {
                    data: k,
                    source: "XmlHttpRequest",
                    url: f
                },
                d.sender)
            },
            function(c, k, g, f) {
                na.raiseEvent("RealTimeUpdateError", {
                    source: "XmlHttpRequest",
                    url: f,
                    xmlHttpRequestObject: k.xhr,
                    error: c,
                    httpStatus: k.xhr && k.xhr.status ? k.xhr.status: -1
                },
                d.sender);
                a.isActive() ? b._toRealtime = setTimeout(h, e) : clearTimeout(b._toRealtime)
            }),
            h(),
            a.addEventListener("beforedispose",
            function() {
                clearTimeout(b._toRealtime)
            })
        });
        X.prototype = {
            getColor: function() {
                var d = this.paletteId,
                c = this.defaultGaugePaletteOptions.paletteColors[d][this._iterator];
                this._iterator += 1;
                if (this._iterator == this.defaultGaugePaletteOptions.paletteColors[d].length - 1) this._iterator = 0;
                return c
            },
            get2DBgColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? R(c, 35) + q + R(c, 10) : this.defaultGaugePaletteOptions.bgColor[d]
            },
            get2DBgAlpha: function() {
                return this.defaultGaugePaletteOptions.bgAlpha[this.paletteId]
            },
            get2DBgAngle: function() {
                return this.defaultGaugePaletteOptions.bgAngle[this.paletteId]
            },
            get2DBgRatio: function() {
                return this.defaultGaugePaletteOptions.bgRatio[this.paletteId]
            },
            getTickColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 90) : this.defaultGaugePaletteOptions.tickColor[d]
            },
            getTrendDarkColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 90) : this.defaultGaugePaletteOptions.trendDarkColor[d]
            },
            getTrendLightColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? R(c, this.defaultGaugePaletteOptions.TrendLightShadeOffset) : this.defaultGaugePaletteOptions.trendLightColor[d]
            },
            get2DCanvasBgColor: function() {
                return this.defaultGaugePaletteOptions.canvasBgColor[this.paletteId]
            },
            get2DCanvasBgAngle: function() {
                return this.defaultGaugePaletteOptions.canvasBgAngle[this.paletteId]
            },
            get2DCanvasBgAlpha: function() {
                return this.defaultGaugePaletteOptions.canvasBgAlpha[this.paletteId]
            },
            get2DCanvasBgRatio: function() {
                return this.defaultGaugePaletteOptions.canvasBgRatio[this.paletteId]
            },
            get2DCanvasBorderColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 80) : this.defaultGaugePaletteOptions.canvasBorderColor[d]
            },
            get2DCanvasBorderAlpha: function() {
                return this.defaultGaugePaletteOptions.canvasBorderAlpha[this.paletteId]
            },
            get2DAltHGridColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? R(c, 20) : this.defaultGaugePaletteOptions.altHGridColor[d]
            },
            get2DAltHGridAlpha: function() {
                return this.defaultGaugePaletteOptions.altHGridAlpha[this.paletteId]
            },
            get2DAltVGridColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? R(c, 80) : this.defaultGaugePaletteOptions.altVGridColor[d]
            },
            get2DAltVGridAlpha: function() {
                return this.defaultGaugePaletteOptions.altVGridAlpha[this.paletteId]
            },
            get2DToolTipBgColor: function() {
                return this.defaultGaugePaletteOptions.toolTipBgColor[this.paletteId]
            },
            get2DToolTipBorderColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 80) : this.defaultGaugePaletteOptions.toolTipBorderColor[d]
            },
            get2DBaseFontColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? c: this.defaultGaugePaletteOptions.baseFontColor[d]
            },
            get2DBorderColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 60) : this.defaultGaugePaletteOptions.borderColor[d]
            },
            get2DBorderAlpha: function() {
                return this.defaultGaugePaletteOptions.borderAlpha[this.paletteId]
            },
            get2DLegendBgColor: function() {
                return this.defaultGaugePaletteOptions.legendBgColor[this.paletteId]
            },
            get2DLegendBorderColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 80) : this.defaultGaugePaletteOptions.legendBorderColor[d]
            },
            get2DPlotGradientColor: function() {
                return this.defaultGaugePaletteOptions.plotGradientColor[this.paletteId]
            },
            get2DPlotBorderColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 85) : this.defaultGaugePaletteOptions.plotBorderColor[d]
            },
            get2DPlotFillColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 85) : this.defaultGaugePaletteOptions.plotFillColor[d]
            },
            get2DMsgLogColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? R(c, 80) : this.defaultGaugePaletteOptions.msgLogColor[d]
            },
            getDialColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 95) + ",FFFFFF," + L(c, 95) : this.defaultGaugePaletteOptions.dialColor[d]
            },
            getDialBorderColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 95) + ",FFFFFF," + L(c, 95) : this.defaultGaugePaletteOptions.dialBorderColor[d]
            },
            getPivotColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? R(c, 95) + ",FFFFFF," + R(c, 95) : this.defaultGaugePaletteOptions.pivotColor[d]
            },
            getPivotBorderColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 95) + ",FFFFFF," + L(c, 95) : this.defaultGaugePaletteOptions.pivotBorderColor[d]
            },
            getPointerBorderColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 75) : this.defaultGaugePaletteOptions.pointerBorderColor[d]
            },
            getPointerBgColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 75) : this.defaultGaugePaletteOptions.pointerBgColor[d]
            },
            getThmBorderColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? L(c, 90) : this.defaultGaugePaletteOptions.thmBorderColor[d]
            },
            getThmFillColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? R(c, 55) : this.defaultGaugePaletteOptions.thmFillColor[d]
            },
            getCylFillColor: function() {
                var d = this.paletteId,
                c = this.themeColor;
                return this.isTheme ? R(c, 55) : this.defaultGaugePaletteOptions.cylFillColor[d]
            },
            getPeriodColor: function() {
                return this.isTheme ? R(this.themeColor, 10) : this.defaultGaugePaletteOptions.periodColor[this.paletteId]
            },
            getWinColor: function() {
                return this.defaultGaugePaletteOptions.winColor[this.paletteId]
            },
            getLossColor: function() {
                return this.defaultGaugePaletteOptions.lossColor[this.paletteId]
            },
            getDrawColor: function() {
                return this.defaultGaugePaletteOptions.drawColor[this.paletteId]
            },
            getScoreLessColor: function() {
                return this.defaultGaugePaletteOptions.scorelessColor[this.paletteId]
            },
            reset: function() {
                this._iterator = 0
            },
            parseColorMix: function(d, c) {
                var a = [],
                c = c.replace(/\s/g, J),
                c = c.toLowerCase();
                if (c == "" || c == null || c == void 0) a = [d];
                else {
                    var b, k, e = c.split(",");
                    for (b = 0; b < e.length; b++) e[b] = e[b].replace("{", ""),
                    e[b] = e[b].replace("}", ""),
                    e[b] == "color" ? a.push(d) : e[b].substr(0, 5) == "light" ? (k = e[b].indexOf("-"), k = k == -1 ? 1 : e[b].substr(k + 1, e[b].length - k), k = 100 - k, a.push(R(d, k))) : e[b].substr(0, 4) == "dark" ? (k = e[b].indexOf("-"), k = k == -1 ? 1 : e[b].substr(k + 1, e[b].length - k), k = 100 - k, a.push(L(d, k))) : a.push(e[b])
                }
                return a
            },
            parseAlphaList: function(d, c) {
                var a = [],
                b = [],
                a = d.split(","),
                k,
                e;
                for (e = 0; e < c; e++) k = a[e],
                k = isNaN(k) || k == void 0 ? 100 : Number(k),
                b[e] = k;
                return b.join()
            },
            parseRatioList: function(d, c) {
                var a = [],
                b = [],
                a = d.split(","),
                k = 0,
                e,
                g;
                for (g = 0; g < c; g++) e = a[g],
                e = isNaN(e) || e == void 0 ? 0 : Math.abs(Number(e)),
                e = e > 100 ? 100 : e,
                b[g] = e,
                k += e;
                k = k > 100 ? 100 : k;
                if (a.length < c) for (g = a.length; g < c; g++) b[g] = (100 - k) / (c - a.length);
                b[ - 1] = 0;
                return b.join()
            }
        };
        X.prototype.constructor = X;
        y = Q.setOptions({}).plotOptions;
        qa.prototype = {
            numDecimals: function(d) {
                d = Ha(d, 2);
                d = Math.abs(d);
                d = String(d - Math.floor(d)).length - 2;
                return d < 0 ? 0 : d
            },
            toRadians: function(d) {
                return d / 180 * Math.PI
            },
            toDegrees: function(d) {
                return d / Math.PI * 180
            },
            flashToStandardAngle: function(d) {
                return - 1 * d
            },
            standardToFlashAngle: function(d) {
                return - 1 * d
            },
            flash180ToStandardAngle: function(d) {
                var c = 360 - ((d %= 360) < 0 ? d + 360 : d);
                return c == 360 ? 0 : c
            },
            getAngularPoint: function(d, c, a, b) {
                b *= Math.PI / 180;
                return {
                    x: d + a * Math.cos(b),
                    y: c - a * Math.sin(b)
                }
            },
            remainderOf: function(d, c) {
                return Fa(d % c)
            },
            boundAngle: function(d) {
                return d >= 0 ? remainderOf(d, 360) : 360 - remainderOf(Math.abs(d), 360)
            },
            toNearestTwip: function(d) {
                var c = Math.round(Math.abs(d) * 100),
                a = Math.floor(c / 5);
                return (d < 0 ? -1 : 1) * ((Number(String(c - a * 5)) > 2 ? a * 5 + 5 : a * 5) / 100)
            },
            roundUp: function(d, c) {
                var a = Math.pow(10, c == void 0 ? 2 : c);
                d *= a;
                d = Math.round(Number(String(d)));
                d /= a;
                return d
            }
        };
        qa.prototype.constructor = qa;
        window.MathExt = qa;
        var ma = function(d, c, a, b, k) {
            this.userMin = d;
            this.userMax = c;
            this.numMajorTM = f(b.majorTMNumber, -1);
            this.numMinorTM = f(b.minorTMNumber, 5);
            this.adjustTM = b.adjustTM;
            this.tickValueStep = f(b.tickValueStep, 1);
            this.showLimits = f(b.showLimits, 1);
            this.showTickValues = f(b.showTickValues, 1);
            this.nf = k;
            this.stopMaxAtZero = a;
            this.setMinAsZero = !b.setAdaptiveMin;
            this.upperLimitDisplay = b.upperLimitDisplay;
            this.lowerLimitDisplay = b.lowerLimitDisplay;
            this.userMaxGiven = this.userMax == null || this.userMax == void 0 || this.userMax == "" ? !1 : !0;
            this.userMinGiven = this.userMin == null || this.userMin == void 0 || this.userMin == "" ? !1 : !0;
            this.majorTM = [];
            this.minorTM = [];
            this.MathExt = new qa
        };
        ma.prototype = {
            setAxisCoords: function(d, c) {
                this.startAxisPos = d;
                this.endAxisPos = c
            },
            calculateLimits: function(d, c) {
                d = isNaN(d) == !0 || d == void 0 ? 0.9 : d;
                c = isNaN(c) == !0 || c == void 0 ? 0 : c;
                d == c && d == 0 && (d = 0.9);
                var a = Math.max(Math.floor(Math.log(Math.abs(c)) / Math.LN10), Math.floor(Math.log(Math.abs(d)) / Math.LN10)),
                b = Math.pow(10, a);
                Math.abs(d) / b < 2 && Math.abs(c) / b < 2 && (a--, b = Math.pow(10, a));
                a = Math.pow(10, Math.floor(Math.log(d - c) / Math.LN10));
                d - c > 0 && b / a >= 10 && (b = a);
                var a = (Math.floor(d / b) + 1) * b,
                k;
                c < 0 ? k = -1 * (Math.floor(Math.abs(c / b)) + 1) * b: this.setMinAsZero ? k = 0 : (k = Math.floor(Math.abs(c / b) - 1) * b, k = k < 0 ? 0 : k);
                this.stopMaxAtZero && d <= 0 && (a = 0);
                this.max = this.userMaxGiven == !1 || this.userMaxGiven == !0 && Number(this.userMax) < d ? a: Number(this.userMax);
                this.min = this.userMinGiven == !1 || this.userMinGiven == !0 && Number(this.userMin) > c ? k: Number(this.userMin);
                this.range = Math.abs(this.max - this.min);
                this.interval = b;
                this.calcTickInterval()
            },
            calcTickInterval: function() {
                if (this.numMajorTM != -1 && this.numMajorTM < 2) this.numMajorTM = 2;
                if (this.userMinGiven == !1 && this.userMaxGiven == !1 && this.numMajorTM != -1) {
                    this.numMajorTM = this.numMajorTM == -1 ? 5 : this.numMajorTM;
                    var d = this.getDivisibleRange(this.min, this.max, this.numMajorTM, this.interval, !0),
                    c = d - this.range;
                    this.range = d;
                    this.max > 0 ? this.max += c: this.min -= c
                } else if (this.numMajorTM = this.numMajorTM == -1 ? 5 : this.numMajorTM, this.adjustTM == !0) {
                    for (var d = 0,
                    c = 1,
                    a;;) {
                        a = this.numMajorTM + d * c;
                        a = a == 0 ? 1 : a;
                        if (this.isRangeDivisible(this.range, a, this.interval)) break;
                        d = c == -1 || d > this.numMajorTM ? ++d: d;
                        if (d > 25) {
                            a = 2;
                            break
                        }
                        c = d <= this.numMajorTM ? c * -1 : 1
                    }
                    this.numMajorTM = a
                }
                this.majorTickInt = (this.max - this.min) / (this.numMajorTM - 1)
            },
            isRangeDivisible: function(d, c, a) {
                return this.MathExt.numDecimals(d / (c - 1)) > this.MathExt.numDecimals(a) ? !1 : !0
            },
            getDivisibleRange: function(d, c, a, b, k) {
                if (a < 3) return this.range;
                d = Math.abs(c - d);
                c = d / (a - 1);
                this.isRangeDivisible(d, a, b) || (k && Number(c) / Number(b) < (b > 1 ? 2 : 0.5) && (b /= 10), c = (Math.floor(c / b) + 1) * b, d = c * (a - 1));
                return d
            },
            calculateTicks: function() {
                this.majorTM = [];
                this.minorTM = [];
                for (var d = 0,
                c, a, b = this.numMajorTM,
                k = this.numMinorTM,
                e = this.nf,
                g = this.tickValueStep,
                h = Z(this.lowerLimitDisplay), l = Z(this.upperLimitDisplay), j = this.majorTickInt, i = this.min, p = this.showTickValues, o = !1, n = f(this.showLimits, p); d < b; d += 1) {
                    c = Ha(Number(i + j * d), 10);
                    a = e.yAxis(c);
                    o = !1;
                    if (d == 0 || d == b - 1) o = !0,
                    a = n != 0 ? d == 0 ? m(h, a) : m(l, a) : J;
                    else if (p == 0 || d % g !== 0) a = J;
                    this.majorTM.push({
                        displayValue: a,
                        isString: o,
                        value: c
                    })
                }
                a = j / (k + 1);
                for (d = 0; d < b - 1; d += 1) for (c = 1; c <= k; c += 1) this.minorTM.push(this.majorTM[d].value + a * c)
            },
            returnDataAsTick: function(d, c) {
                var a = {};
                a.value = d;
                a.displayValue = this.nf.dataLabels(d);
                a.showValue = c;
                return a
            },
            getMax: function() {
                return this.max
            },
            getMin: function() {
                return this.min
            },
            getMajorTM: function() {
                return this.majorTM
            },
            getMinorTM: function() {
                return this.minorTM
            },
            getAxisPosition: function(d) {
                if (this.startAxisPos == void 0 || this.endAxisPos == void 0) throw Error("Cannot calculate position, as axis co-ordinates have not been defined. Please use setAxisCoords() method to define the same.");
                return this.startAxisPos + (this.endAxisPos - this.startAxisPos) / (this.max - this.min) * (d - this.min)
            },
            getValueFromPosition: function(d) {
                if (this.startAxisPos == void 0 || this.endAxisPos == void 0) throw Error("Cannot calculate value, as axis co-ordinates have not been defined. Please use setAxisCoords() method to define the same.");
                var c, a;
                c = this.max - this.min;
                a = d - this.startAxisPos;
                return a / (a + (this.endAxisPos - d)) * c + this.min
            }
        };
        x("widgetbase", {
            creditLabel: !1,
            draw: function() {}
        },
        x.linebase);
        x("angulargauge", {
            standaloneInit: !0,
            drawAnnotations: !0,
            defaultSeriesType: "angulargauge",
            creditLabel: !1,
            realtimeEnabled: !0,
            eiMethods: {
                feedData: function() {
                    console.warn("data fed: ", arguments)
                }
            },
            linearDataParser: function(d) {
                var c = 0,
                a, b, k = {},
                d = d.split("&");
                for (a = d.length; c < a; c += 1) if (b = d[c].split("="), !(b[0] === "" || b[1] === void 0 || b[1] === "")) switch (b[0]) {
                case "label":
                    k.labels = b[1].split("|");
                    break;
                case "value":
                    k.values = b[1].split("|");
                    break;
                case "showLabel":
                    k.showLabels = b[1].split("|");
                    break;
                case "toolText":
                    k.toolTexts = b[1].split("|");
                    break;
                case "link":
                    k.links = b[1].split("|");
                    break;
                case "color":
                    k.colors = b[1].split("|")
                }
                return k
            },
            defaultGaugePaletteOptions: S(Y, {
                dialColor: ["999999,ffffff,999999", "ADB68F,F3F5DD,ADB68F", "A2C4C8,EDFBFE,A2C4C8", "FDB548,FFF5E8,FDB548", "FF7CA0,FFD1DD,FF7CA0"],
                dialBorderColor: ["999999", "ADB68F", "A2C4C8", "FDB548", "FF7CA0"],
                pivotColor: ["999999,ffffff,999999", "ADB68F,F3F5DD,ADB68F", "A2C4C8,EDFBFE,A2C4C8", "FDB548,FFF5E8,FDB548", "FF7CA0,FFD1DD,FF7CA0"],
                pivotBorderColor: ["999999", "ADB68F", "A2C4C8", "FDB548", "FF7CA0"]
            }),
            subTitleFontSizeExtender: 0,
            charttopmargin: 5,
            chartrightmargin: 5,
            chartbottommargin: 5,
            chartleftmargin: 5,
            defaultPlotShadow: 1,
            series: function(d, c, a, b, k) {
                var a = {
                    data: [],
                    colorByPoint: !0
                },
                e = d.chart,
                g = c[K],
                h = c.chart,
                l = this.numberFormatter = g.numberFormatter,
                j = T({},
                d.colorrange),
                i = new da(j.color, void 0, this.defaultGaugePaletteOptions.paletteColors[h.paletteIndex]),
                p = new X(h.paletteIndex, e.palettethemecolor, this),
                o,
                n = a.showValue = f(e.showvalue, e.showrealtimevalue, 0),
                j = 1,
                C = 0,
                v,
                s,
                u = d.dials && d.dials.dial;
                b -= h.marginRight + h.marginLeft;
                k -= h.marginTop + h.marginBottom;
                if (u) {
                    k -= fa(c, d, b, k / 2);
                    a.origW = C = f(e.origw, b);
                    a.origH = d = f(e.origh, k);
                    j = (a.autoScale = j = f(e.autoscale, 1)) ? ha(C, d, b, k) : 1;
                    a.scaleFactor = j;
                    var w = f(e.editmode, 0),
                    d = C = f(z(e.pivotradius) * j, 5);
                    h.plotBorderColor = O;
                    h.plotBackgroundColor = O;
                    a.pivotRadius = d;
                    var r = 0,
                    t = u.length,
                    A, P = !0,
                    D, I, F, E, B, h = 0;
                    if (t) for (; r < t; r += 1) A = u[r],
                    o = l.getCleanValue(A.value),
                    typeof o != "undefined" && o != null && (P && (s = D = o, P = !1), s = Math.max(o, s), D = Math.min(o, D)),
                    I = f(z(A.rearextension) * j, 0),
                    C = Math.max(C, I),
                    F = l.dataLabels(o),
                    E = f(A.showvalue, n),
                    B = f(z(A.valuey) * j),
                    E && !U(B) && (h += 1),
                    a.data.push({
                        y: o,
                        id: m(A.id, r),
                        color: {
                            FCcolor: {
                                color: m(A.color, A.bgcolor, p.getDialColor()),
                                angle: 90
                            }
                        },
                        showValue: E,
                        editMode: f(A.editmode, w),
                        borderColor: G(m(A.bordercolor, p.getDialBorderColor()), f(A.borderalpha, 100)),
                        borderThickness: f(A.borderthickness, 1),
                        baseWidth: f(z(A.basewidth) * j, d * 1.6),
                        topWidth: f(z(A.topwidth) * j, 0),
                        rearExtension: I,
                        valueX: f(z(A.valuex) * j),
                        valueY: B,
                        radius: f(z(A.radius) * j),
                        link: m(A.link, J),
                        toolText: m(A.tooltext, A.hovertext, F),
                        displayValue: E ? F: J,
                        doNotSlice: !0
                    });
                    a.colorRangeManager = i;
                    u = f(e.gaugescaleangle, 180);
                    l = z(e.gaugestartangle);
                    n = z(e.gaugeendangle);
                    w = U(l);
                    o = Ca ? 0.0010 : 0.01;
                    r = U(n);
                    if (u > 360 || u < -360) u = u > 0 ? 360 : -360;
                    if (n > 360 || n < -360) n %= 360;
                    if (l > 360 || l < -360) l %= 360;
                    if (w && r) {
                        if (u = l - n, u > 360 || u < -360) u %= 360,
                        n = l - u
                    } else if (w) {
                        if (n = l - u, n > 360 || n < -360) n %= 360,
                        l += n > 0 ? -360 : 360
                    } else if (r) {
                        if (l = n + u, l > 360 || l < -360) l %= 360,
                        n += l > 0 ? -360 : 360
                    } else u === 360 ? (l = 180, n = -180) : u === -360 ? n = l = -180 : (l = 90 + u / 2, n = l - u);
                    Math.abs(u) === 360 && (u += u > 0 ? -o: o, n = l - u);
                    n = 360 - n;
                    l = 360 - l;
                    if (l > 360 || n > 360) l -= 360,
                    n -= 360;
                    a.gaugeStartAngle = l *= ka;
                    a.gaugeEndAngle = n *= ka;
                    a.gaugeScaleAngle = -u * ka;
                    a.setAdaptiveMin = f(e.setadaptivemin, 0);
                    a.upperLimitDisplay = e.upperlimitdisplay;
                    a.lowerLimitDisplay = e.lowerlimitdisplay;
                    a.showTickMarks = f(e.showtickmarks, 1);
                    a.showTickValues = f(e.showtickvalues, a.showTickMarks);
                    a.showLimits = f(e.showlimits, a.showTickValues);
                    a.placeTicksInside = f(e.placeticksinside, 0);
                    a.placeValuesInside = f(e.placevaluesinside, 0);
                    a.adjustTM = f(e.adjusttm, 1);
                    a.majorTMNumber = f(e.majortmnumber, -1);
                    a.majorTMColor = m(e.majortmcolor, p.getTickColor());
                    a.majorTMAlpha = f(e.majortmalpha, 100);
                    a.majorTMHeight = f(z(e.majortmheight) * j, 6);
                    a.majorTMThickness = f(e.majortmthickness, 1);
                    a.minorTMNumber = f(e.minortmnumber, 4);
                    a.minorTMColor = m(e.minortmcolor, a.majorTMColor);
                    a.minorTMAlpha = f(e.minortmalpha, a.majorTMAlpha);
                    a.minorTMHeight = f(z(e.minortmheight) * j, Math.round(a.majorTMHeight / 2));
                    a.minorTMThickness = f(e.minortmthickness, 1);
                    a.tickValueDistance = f(e.tickvaluedistance, e.displayvaluedistance, 15) * j + 2;
                    a.trendValueDistance = f(z(e.trendvaluedistance) * j, a.tickValueDistance);
                    a.tickValueStep = f(e.tickvaluestep, e.tickvaluesstep, 1);
                    a.tickValueStep = a.tickValueStep < 1 ? 1 : a.tickValueStep;
                    u = a.valueBelowPivot = f(e.valuebelowpivot, 0);
                    w = f(parseInt(c.plotOptions.series.dataLabels.style.lineHeight, 10), 12);
                    g = new ma(e.lowerlimit, e.upperlimit, !1, a, g.numberFormatter);
                    i = i.colorArr;
                    o = i.length - 1;
                    i[0] && U(i[0].minvalue) && (D = Math.min(D, i[0].minvalue));
                    i[o] && U(i[o].maxvalue) && (s = Math.min(s, i[o].maxvalue));
                    g.calculateLimits(s, D);
                    a.min = g.min;
                    a.max = g.max;
                    g.calculateTicks();
                    a.majorTM = g.getMajorTM();
                    a.minorTM = g.getMinorTM();
                    a.annRenderDelay = e.annrenderdelay;
                    a.useMessageLog = f(e.usemessagelog, 0);
                    a.messageLogWPercent = f(e.messagelogwpercent, 80);
                    a.messageLogHPercent = f(e.messageloghpercent, 70);
                    a.messageLogShowTitle = f(e.messagelogshowtitle, 1);
                    a.messageLogTitle = m(e.messagelogtitle, "Message Log");
                    a.messageLogColor = m(e.messagelogcolor, p.get2DMsgLogColor());
                    a.messageGoesToLog = f(e.messagegoestolog, 1);
                    a.messageGoesToJS = f(e.messagegoestojs, 0);
                    a.messageJSHandler = m(e.messagejshandler, "alert");
                    a.messagePassAllToJS = f(e.messagepassalltojs, 0);
                    a.dataStreamURL = unescape(m(e.datastreamurl, ""));
                    a.streamURLQMarkPresent = a.dataStreamURL.indexOf("?") != -1;
                    a.refreshInterval = f(e.refreshinterval, 1);
                    a.dataStamp = m(e.datastamp, "");
                    a.gaugeFillMix = e.gaugefillmix;
                    a.gaugeFillRatio = e.gaugefillratio;
                    if (a.gaugeFillMix == void 0) a.gaugeFillMix = "{light-10},{light-70},{dark-10}";
                    if (a.gaugeFillRatio == void 0) a.gaugeFillRatio = ",6";
                    else if (a.gaugeFillRatio != "") a.gaugeFillRatio = "," + a.gaugeFillRatio;
                    a.showGaugeBorder = f(e.showgaugeborder, 1);
                    a.gaugeBorderColor = m(e.gaugebordercolor, "{dark-20}");
                    a.gaugeBorderThickness = f(e.gaugeborderthickness, 1);
                    a.gaugeBorderAlpha = m(e.gaugeborderalpha, V);
                    s = p.parseColorMix(m(e.pivotfillcolor, e.pivotcolor, e.pivotbgcolor, p.getPivotColor()), m(e.pivotfillmix, "{light-10},{light-30},{dark-20}"));
                    a.pivotFillAlpha = p.parseAlphaList(m(e.pivotfillalpha, V), s.length);
                    a.pivotFillRatio = p.parseRatioList(m(e.pivotfillratio, ga), s.length);
                    a.pivotFillColor = s.join();
                    a.pivotFillAngle = f(e.pivotfillangle, 0);
                    a.isRadialGradient = m(e.pivotfilltype, "radial").toLowerCase() == "radial";
                    a.showPivotBorder = f(e.showpivotborder, 0);
                    a.pivotBorderThickness = f(e.pivotborderthickness, 1);
                    a.pivotBorderColor = G(m(e.pivotbordercolor, p.getPivotBorderColor()), a.showPivotBorder == 1 ? m(e.pivotborderalpha, V) : ga);
                    this.parseColorMix = p.parseColorMix;
                    this.parseAlphaList = p.parseAlphaList;
                    this.parseRatioList = p.parseRatioList;
                    D = a.tickValueStyle = c.yAxis[0].labels.style;
                    s = f(parseInt(D.fontSize, 10), 10);
                    d = h * w + 2 + d;
                    w = 0;
                    u || (w = d, d = 0);
                    a.gaugeOuterRadius = f(z(e.gaugeouterradius) * j);
                    r = a.gaugeStartAngle;
                    t = a.gaugeEndAngle;
                    p = k;
                    h = a.gaugeOuterRadius;
                    g = f(z(e.gaugeoriginx) * j);
                    i = f(z(e.gaugeoriginy) * j);
                    C = Math.max(C, s);
                    u = w;
                    w = U(h);
                    I = U(g);
                    o = U(i);
                    F = Math.PI * 2;
                    E = Math.PI;
                    B = Math.PI / 2;
                    var q = E + B,
                    h = {
                        radius: h,
                        centerX: g,
                        centerY: i
                    },
                    H,
                    x,
                    y,
                    P = !1,
                    ca,
                    M = r % F;
                    M < 0 && (M += F); (C = C || 0) && C < b / 2 && C < p / 2 && (P = !0);
                    d > p / 2 && (d = p / 2);
                    u > p / 2 && (u = p / 2);
                    H = Math.cos(r);
                    y = Math.sin(r);
                    x = Math.cos(t);
                    ca = Math.sin(t);
                    A = Math.min(H, x, 0);
                    x = Math.max(H, x, 0);
                    H = Math.min(y, ca, 0);
                    y = Math.max(y, ca, 0);
                    if (!w || !I || !o) {
                        t -= r;
                        r = M + t;
                        if (r > F || r < 0) x = 1;
                        if (t > 0) {
                            if (M < B && r > B || r > F + B) y = 1;
                            if (M < E && r > E || r > F + E) A = -1;
                            if (M < q && r > q || r > F + q) H = -1
                        } else {
                            if (M > B && r < B || r < -q) y = 1;
                            if (M > E && r < E || r < -E) A = -1;
                            if (M > q && r < q || r < -B) H = -1
                        }
                        I ? w || (t = b - g, v = A ? Math.min(t / x, -g / A) : t / x) : (r = b / (x - A), g = -r * A, v = r, P && (b - g < C ? (g = b - C, t = b - g, v = A ? Math.min(t / x, -g / A) : t / x) : g < C && (g = C, t = b - g, v = A ? Math.min(t / x, -g / A) : t / x)), h.centerX = g);
                        o ? w || (t = p - i, v = Math.min(v, H ? Math.min(t / y, -i / H) : t / y)) : (r = p / (y - H), i = -r * H, P && (p - i < C ? (i = p - C, t = p - i, v = Math.min(v, H ? Math.min(t / y, -i / H) : t / y)) : i < C && (i = C, t = p - i, v = Math.min(v, H ? Math.min(t / y, -i / H) : t / y))), p - i < d ? (i = p - d, t = p - i, v = Math.min(v, H ? Math.min(t / y, -i / H) : t / y)) : i < u && (i = u, t = p - i, v = Math.min(v, H ? Math.min(t / y, -i / H) : t / y)), v = Math.min(v, r), h.centerY = i);
                        h.maxRadius = v
                    }
                    v = a.gaugeOriginX = h.centerX;
                    C = a.gaugeOriginY = h.centerY;
                    p = A = h.maxRadius;
                    g = 20;
                    i = a.majorTM;
                    d = 0;
                    h = i.length;
                    u = c.labels.smartLabel;
                    w = a.min;
                    b -= v;
                    k -= C;
                    o = A * Math.cos(89.99 * ka);
                    r = !1;
                    t = a.tickValueDistance;
                    n = (n - l) / (a.max - a.min);
                    g + t < A ? g += t: r = !0;
                    if (!U(a.gaugeOuterRadius)) {
                        u.setStyle(D);
                        if (a.showTickValues || a.showLimits) for (; d < h; d += 1) if (D = i[d], P = l + (D.value - w) * n, A = Math.cos(P), P = Math.sin(P), I = p * A, F = p * P, E = D.displayValue, B = u.getOriSize(E), E = B.width, B = B.height, E > 0 && B > 0) if (D.y = s - B / 2, a.placeValuesInside) I > o ? D.x = -E / 2 : I < -o ? D.x = E / 2 : (D.x = 0, D.y += F < o ? B / 2 : -B / 2);
                        else {
                            if (I > o) {
                                if (D.x = E / 2, I + E > b) if (b > E) {
                                    if (I = b - E, p = I / A, p < g) {
                                        p = g;
                                        break
                                    }
                                } else {
                                    p = g;
                                    break
                                }
                            } else if (I < -o) {
                                if (D.x = -E / 2, E - I > v) if (v > E) {
                                    if (I = E - v, p = I / A, p < g) {
                                        p = g;
                                        break
                                    }
                                } else {
                                    p = g;
                                    break
                                }
                            } else D.y += F > o ? 0 : -B / 2;
                            if (F > o) {
                                if (F + B > k) if (k > B) {
                                    if (F = k - B, p = F / P, p < g) {
                                        p = g;
                                        break
                                    }
                                } else {
                                    p = g;
                                    break
                                }
                            } else if (F < -o && B - F > C) if (C > B) {
                                if (F = B - C, p = F / P, p < g) {
                                    p = g;
                                    break
                                }
                            } else {
                                p = g;
                                break
                            }
                        }
                        r && (t = p - g);
                        a.gaugeOuterRadius = p - t
                    }
                    a.gaugeInnerRadius = f(z(e.gaugeinnerradius) * j, a.gaugeOuterRadius * 0.7);
                    c.series[0] = a
                }
            },
            spaceManager: function() {}
        },
        ja);
        x("bulb", {
            defaultSeriesType: "bulb",
            defaultPlotShadow: 1,
            standaloneInit: !0,
            drawAnnotations: !0,
            defaultGaugePaletteOptions: Y,
            charttopmargin: 10,
            chartrightmargin: 10,
            chartbottommargin: 10,
            chartleftmargin: 10,
            realtimeEnabled: !0,
            linearDataParser: x.angulargauge.linearDataParser,
            series: function(d, c) {
                var a = {
                    data: [],
                    colorByPoint: !0
                },
                b = d.chart,
                k = c.chart,
                e = this.numberFormatter = c[K].numberFormatter,
                g = T({},
                d.colorrange),
                h = new da(g.color, void 0, k.paletteIndex),
                g = new X(k.paletteIndex, b.palettethemecolor, this),
                l = e.getCleanValue(d.value, !0) || 0,
                j = f(b.showvalue, b.showrealtimevalue, 1),
                i = J,
                p = J,
                i = h.getColorObj(l),
                o = i.isOnMeetPoint ? i.nextObj: i.colorObj,
                n,
                C,
                p = Y.paletteColors[0];
                n = p.length;
                o || (o = i.nextObj || i.prevObj);
                a.colorRangeGetter = h;
                a.defaultColors = p;
                a.defaultColLen = n;
                a.upperLimit = z(b.upperlimit);
                a.lowerLimit = z(b.lowerlimit);
                a.autoScale = f(b.autoscale, 1);
                a.origW = z(b.origw);
                a.origH = z(b.origh);
                a.annRenderDelay = z(b.annrenderdelay);
                a.useMessageLog = f(b.usemessagelog, 0);
                a.messageLogWPercent = f(b.messagelogwpercent, 80);
                a.messageLogHPercent = f(b.messageloghpercent, 70);
                a.messageLogShowTitle = f(b.messagelogshowtitle, 1);
                a.messageLogTitle = m(b.messagelogtitle, "Message Log");
                a.messageLogColor = m(b.messagelogcolor, g.get2DMsgLogColor());
                a.messageGoesToLog = f(b.messagegoestolog, 1);
                a.messageGoesToJS = f(b.messagegoestojs, 0);
                a.messageJSHandler = m(b.messagejshandler, "alert");
                a.messagePassAllToJS = f(b.messagepassalltojs, 0);
                a.showValue = j;
                a.useColorNameAsValue = f(b.usecolornameasvalue, 0);
                a.placeValuesInside = f(b.placevaluesinside, 0);
                a.valuePadding = f(b.valuepadding, a.placeValuesInside ? 0 : 4);
                a.dataStreamURL = m(b.datastreamurl, J);
                a.streamURLQMarkPresent = a.dataStreamURL && a.dataStreamURL.indexOf("?") != -1;
                a.refreshInterval = f(b.refreshinterval, 1);
                a.dataStamp = m(b.datastamp, "");
                k.backgroundColor = {
                    FCcolor: {
                        color: m(b.bgcolor, g.get2DBgColor()),
                        alpha: m(b.bgalpha, g.get2DBgAlpha()),
                        angle: m(b.bgangle, g.get2DBgAngle()),
                        ratio: m(b.bgratio, g.get2DBgRatio())
                    }
                };
                k.borderColor = G(m(b.bordercolor, g.get2DBorderColor()), m(b.borderalpha, g.get2DBorderAlpha()));
                c.plotOptions.series.dataLabels.style.color = c.title.style.color = c.subtitle.style.color = aa(m(b.basefontcolor, g.get2DBaseFontColor()));
                c.plotOptions.series.dataLabels.style.fontWeight = "bold";
                i = c.tooltip.style;
                c.tooltip.backgroundColor = G(m(i.backgroundColor, b.tooltipbgcolor, b.hovercapbgcolor, b.hovercapbg, g.get2DToolTipBgColor()), m(b.tooltipbgalpha, 100));
                c.tooltip.borderColor = G(m(i.borderColor, b.tooltipbordercolor, b.hovercapbordercolor, b.hovercapborder, g.get2DToolTipBorderColor()), m(b.tooltipborderalpha, 100));
                i = f(b.showgaugeborder, 0);
                k = m(o.bordercolor, b.gaugebordercolor, L(o.code, 70));
                h = f(b.gaugeborderthickness, 1);
                n = m(b.gaugefillalpha, o.alpha, V);
                C = i == 1 ? m(o.borderalpha, b.gaugeborderalpha, "90") * n / 100 : 0;
                a.gaugeOriginX = f(b.gaugeoriginx, b.bulboriginx, -1);
                a.gaugeOriginY = f(b.gaugeoriginy, b.bulboriginy, -1);
                a.gaugeRadius = f(b.gaugeradius, b.bulbradius, -1);
                a.is3D = f(b.is3d, 1);
                i = b = e.dataLabels(l);
                p = Z(m(o.label, o.name));
                j == 0 && (p = i = J);
                e = a.is3D ? this.getPointColor(o.code, n) : G(o.code, n);
                k = G(/\{/.test(k) ? g.parseColorMix(m(o.bordercolor, o.code), k)[0] : k, C);
                a.data[0] = {
                    y: l,
                    displayValue: i,
                    colorName: p,
                    toolText: b,
                    color: e,
                    borderWidth: h,
                    borderColor: k,
                    colorRange: o,
                    doNotSlice: !0
                };
                c.legend.enabled = !1;
                HCData = a.data;
                c.chart.plotBackgroundColor = O;
                c.chart.plotBorderColor = O;
                a instanceof Array ? c.series = c.series.concat(a) : c.series.push(a);
                this.configureAxis(c, d);
                d.trendlines && ia(d.trendlines, c.yAxis, c[K], !1, this.isBar)
            },
            getPointColor: function(d, c) {
                var a, b, k = m(c, "60"),
                e = m(c, V),
                d = Qa(d);
                a = R(d, 65);
                b = L(d, 65);
                return {
                    FCcolor: {
                        gradientUnits: "objectBoundingBox",
                        cx: 0.4,
                        cy: 0.4,
                        r: "80%",
                        color: a + q + b,
                        alpha: k + q + e,
                        ratio: Wa,
                        radialGradient: !0
                    }
                }
            },
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel,
                e = d.series[0],
                g = e && e.data[0],
                h = d.chart,
                l = a - (h.marginRight + h.marginLeft),
                j = b - (h.marginTop + h.marginBottom),
                i = h.marginLeft,
                p = h.marginTop,
                o = f(c.chart.valuepadding, 4),
                h = e.gaugeRadius !== -1,
                n = 0,
                m = g.displayValue,
                v = e.useColorNameAsValue == 1;
                j -= c = fa(d, c, l, j * 0.3);
                p += c;
                e.dataLabels = {
                    style: d.plotOptions.series.dataLabels.style
                };
                d = e.dataLabels.style;
                a = e.autoScale ? ha(e.origW, e.origH, a, b) : 1;
                e.scaleFactor = a;
                k.setStyle(d);
                if (v) m = g.colorName;
                e.placeValuesInside == 1 ? (b = h ? e.gaugeRadius * a: Math.min(l, j) / 2, c = Math.sqrt(Math.pow(b * 2, 2) / 2), k = k.getSmartText(m, c, c)) : (c = (h ? j - e.gaugeRadius * 2 * a: j * 0.7) - o, k = k.getSmartText(m, l, c), n = k.height, b = Math.min(l, j - n) / 2);
                if (v) g.displayValue = k.text;
                e.valuePadding = o;
                e.valueTextHeight = k.height;
                e.labelLineHeight = parseInt(d.lineHeight);
                g = i + l / 2;
                j = p + (j - n) / 2;
                h && (b = e.gaugeRadius * a);
                e.gaugeOriginX != -1 && (g = e.gaugeOriginX * a);
                e.gaugeOriginY != -1 && (j = e.gaugeOriginY * a);
                e.gaugeRadius = b;
                e.gaugeOriginX = g;
                e.gaugeOriginY = j
            },
            creditLabel: !1
        },
        ja);
        x("cylinder", {
            defaultSeriesType: "cylinder",
            defaultPlotShadow: 1,
            standaloneInit: !0,
            drawAnnotations: !0,
            defaultGaugePaletteOptions: S(Y, {
                cylFillColor: ["CCCCCC", "ADB68F", "E1F5FF", "FDB548", "FF7CA0"],
                periodColor: ["EEEEEE", "ECEEE6", "E6ECF0", "FFF4E6", "FFF2F5"]
            }),
            realtimeEnabled: !0,
            linearDataParser: x.angulargauge.linearDataParser,
            eiMethods: {
                feedData: function() {
                    console.warn("data fed: ", arguments)
                }
            },
            series: function(d, c) {
                var a = {
                    data: [],
                    colorByPoint: !0
                },
                b = d.chart,
                k,
                e = c[K],
                g = this.numberFormatter = e.numberFormatter,
                h = c.chart,
                l = T({},
                d.colorrange),
                j = new da(l.color, void 0, h.paletteIndex),
                i = new X(c.chart.paletteIndex, b.palettethemecolor, this),
                l = g.getCleanValue(d.value) || 0,
                g = f(b.showvalue, 1) ? g.dataLabels(l) : J,
                p = j.getColorObj(l),
                j = p.isOnMeetPoint ? p.nextObj: p.colorObj,
                o = f(b.showvalue, b.showrealtimevalue, 1),
                n = f(b.placevaluesinside, 0);
                a.tickValueStyle = c.yAxis[0].labels.style;
                k = f(this.isPyramid ? b.pyramidyscale: b.funnelyscale);
                a.yScale = k >= 0 && k <= 40 ? k / 200 : 0.2;
                j || (j = p.nextObj || p.prevObj);
                k = Z(j.label);
                h.backgroundColor = {
                    FCcolor: {
                        color: m(b.bgcolor, i.get2DBgColor()),
                        alpha: m(b.bgalpha, i.get2DBgAlpha()),
                        angle: m(b.bgangle, i.get2DBgAngle()),
                        ratio: m(b.bgratio, i.get2DBgRatio())
                    }
                };
                h.borderColor = G(m(b.bordercolor, i.get2DBorderColor()), m(b.borderalpha, i.get2DBorderAlpha()));
                c.plotOptions.series.dataLabels.style.color = c.title.style.color = c.subtitle.style.color = a.tickValueStyle.color = aa(m(b.basefontcolor, i.get2DBaseFontColor()));
                c.plotOptions.series.dataLabels.style.fontWeight = "bold";
                a.setAdaptiveMin = f(b.setadaptivemin, 0);
                a.upperLimit = z(b.upperlimit);
                a.lowerLimit = z(b.lowerlimit);
                a.upperLimitDisplay = z(b.upperlimitdisplay);
                a.lowerLimitDisplay = z(b.lowerlimitdisplay);
                a.autoScale = f(b.autoscale, 1);
                a.origW = z(b.origw);
                a.origH = z(b.origh);
                a.annRenderDelay = z(b.annrenderdelay);
                a.ticksOnRight = f(b.ticksonright, 1);
                a.showTickMarks = f(b.showtickmarks, 1);
                a.showTickValues = f(b.showtickvalues, a.showTickMarks);
                a.showLimits = f(b.showlimits, a.showTickValues);
                a.adjustTM = f(b.adjusttm, 1);
                a.majorTMNumber = f(b.majortmnumber, -1);
                a.majorTMColor = m(b.majortmcolor, i.getTickColor());
                a.majorTMAlpha = f(b.majortmalpha, 100);
                a.majorTMHeight = f(b.majortmheight, b.majortmwidth, 6);
                a.majorTMThickness = f(b.majortmthickness, 1);
                a.minorTMNumber = f(b.minortmnumber, 4);
                a.minorTMColor = m(b.minortmcolor, a.majorTMColor);
                a.minorTMAlpha = f(b.minortmalpha, a.majorTMAlpha);
                a.minorTMHeight = f(b.minortmheight, b.minortmwidth, Math.round(a.majorTMHeight / 2));
                a.minorTMThickness = f(b.minortmthickness, 1);
                a.tickMarkDistance = f(b.tickmarkdistance, b.tickmarkgap, 1);
                a.tickValueDistance = f(b.tickvaluedistance, b.displayvaluedistance, 2);
                a.tickValueStep = f(b.tickvaluestep, b.tickvaluesstep, 1);
                a.tickValueStep = a.tickValueStep < 1 ? 1 : a.tickValueStep;
                a.useMessageLog = f(b.usemessagelog, 0);
                a.messageLogWPercent = f(b.messagelogwpercent, 80);
                a.messageLogHPercent = f(b.messageloghpercent, 70);
                a.messageLogShowTitle = f(b.messagelogshowtitle, 1);
                a.messageLogTitle = m(b.messagelogtitle, "Message Log");
                a.messageLogColor = m(b.messagelogcolor, i.get2DMsgLogColor());
                a.messageGoesToLog = f(b.messagegoestolog, 1);
                a.messageGoesToJS = f(b.messagegoestojs, 0);
                a.messageJSHandler = m(b.messagejshandler, "alert");
                a.messagePassAllToJS = f(b.messagepassalltojs, 0);
                a.showValue = f(b.showvalue, b.showrealtimevalue, 1);
                a.valuePadding = f(b.valuepadding, 4);
                a.dataStreamURL = unescape(m(b.datastreamurl, ""));
                a.streamURLQMarkPresent = a.dataStreamURL.indexOf("?") != -1;
                a.refreshInterval = f(b.refreshinterval, 1);
                a.dataStamp = m(b.datastamp, "");
                a.cylOriginX = z(b.cyloriginx);
                a.cylOriginY = z(b.cyloriginy);
                a.cylRadius = z(b.cylradius);
                a.cylHeight = z(b.cylheight);
                a.cylYScale = f(b.cylyscale, 30);
                if (a.cylYScale > 50 || a.cylYScale < 0) a.cylYScale = 30;
                a.cylYScale /= 100;
                a.cylFillColor = m(b.gaugefillcolor, b.cylfillcolor, i.getCylFillColor());
                a.cylGlassColor = m(b.cylglasscolor, "FFFFFF");
                e = new ma(b.lowerlimit, b.upperlimit, !1, a, e.numberFormatter);
                e.calculateLimits(l, l);
                e.calculateTicks();
                a.majorTM = e.getMajorTM();
                a.minorTM = e.getMinorTM();
                a.min = e.min;
                a.max = e.max;
                e = f(b.is3d, 1);
                b = f(b.usecolornameasvalue, 0);
                a.is3D = e;
                a.placeValuesInside = n;
                a.showValue = o;
                a.data[0] = {
                    y: l,
                    displayValue: b ? k: g,
                    color: G(j.code, j.alpha * 1),
                    colorRange: j
                };
                c.legend.enabled = !1;
                HCData = a.data;
                c.chart.plotBackgroundColor = O;
                c.chart.plotBorderColor = O;
                a instanceof Array ? c.series = c.series.concat(a) : c.series.push(a);
                this.configureAxis(c, d);
                d.trendlines && ia(d.trendlines, c.yAxis, c[K], !1, this.isBar)
            },
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel,
                e = d.series[0],
                g;
                g = e && e.data[0];
                var h, l = d.chart,
                j, i = c.chart,
                p = a - (l.marginRight + l.marginLeft),
                b = j = b - (l.marginTop + l.marginBottom);
                h = l.marginRight;
                var o = l.marginLeft,
                l = l.marginTop,
                i = f(i.valuepadding, 4),
                n = b * 0.7 - i,
                m = a - (h + o),
                v = 0,
                a = 0;
                e.dataLabels = {
                    style: d.plotOptions.series.dataLabels.style
                };
                h = e.dataLabels.style;
                k.setStyle(h);
                g = k.getSmartText(g.displayValue, m, n);
                k = e.autoScale ? ha(e.origW, e.origH, p, b) : 1;
                e.scaleFactor = k;
                c = fa(d, c, p, j / 2);
                b -= c;
                d = pa(d, p, b);
                e.ticksOnRight == 1 ? v = d: a = d;
                d = 0;
                e.showValue && !e.placeValuesInside && (d = i + g.height);
                e.valuePadding = i;
                e.valueTextHeight = g.height;
                e.labelLineHeight = h.lineHeight.split(/px/)[0];
                p = Math.max((p - (a + v)) / 2, 5);
                e.cylRadius = f(e.cylRadius * k, p);
                e.cylHeight = f(e.cylHeight * k, b - d - e.cylRadius * 2.15 * e.cylYScale);
                e.cylOriginX = f(e.cylOriginX * k, o + a) + e.cylRadius;
                p = e.yScaleRadius = e.cylRadius * e.cylYScale;
                o = e.cylinderTotalHeight = p * 2 + e.cylHeight + 10;
                e.cylOriginY = f(e.cylOriginY * k - e.cylHeight, p + (b - o - d + l)) + c
            },
            creditLabel: !1
        },
        ja);
        x("drawingpad", {
            standaloneInit: !0,
            defaultSeriesType: "drawingpad",
            defaultPlotShadow: 1,
            drawAnnotations: !0,
            defaultGaugePaletteOptions: Y,
            series: function(d, c) {
                var a = {
                    data: [],
                    colorByPoint: !0
                };
                c.legend.enabled = !1;
                HCData = a.data;
                c.chart.plotBackgroundColor = O;
                c.chart.plotBorderColor = O;
                a instanceof Array ? c.series = c.series.concat(a) : c.series.push(a);
                this.configureAxis(c, d);
                d.trendlines && ia(d.trendlines, c.yAxis, c[K], !1, this.isBar)
            },
            spaceManager: function() {},
            creditLabel: !1
        },
        x.bulb);
        x("funnel", {
            standaloneInit: !0,
            defaultSeriesType: "funnel",
            defaultPlotShadow: 1,
            subTitleFontSizeExtender: 0,
            drawAnnotations: !0,
            defaultGaugePaletteOptions: Y,
            point: function(d, c, a, b, k) {
                var e, g, h, l, j, i, p, o, d = 0,
                n, C = 0,
                v = [],
                s = m(b.plotborderthickness, la);
                f(b.use3dlighting, 1);
                f(b.showzeropies, 1);
                var u = !0;
                g = k.chart;
                var w = this.isPyramid,
                r = f(b.showpercentintooltip, 1),
                t = f(b.showlabels, 1),
                A = f(b.showvalues, 1),
                P = f(b.showpercentvalues, b.showpercentagevalues, 0),
                D = m(b.tooltipsepchar, b.hovercapsepchar, Xa),
                I = m(b.labelsepchar, D),
                F = m(b.plotbordercolor, b.piebordercolor),
                E = k[K],
                B = E.smartLabel,
                E = E.numberFormatter,
                y = a.length,
                H = k.plotOptions.series.dataLabels,
                x = new X(k.chart.paletteIndex, b.palettethemecolor, this),
                L,
                ca;
                c.isPyramid = w;
                ca = c.streamlinedData = f(b.streamlineddata, 1);
                c.is2d = f(b.is2d, 0);
                c.isHollow = f(b.ishollow, ca ? 1 : 0);
                L = f(b.percentofprevious, 0);
                e = f(this.isPyramid ? b.pyramidyscale: b.funnelyscale);
                c.labelDistance = Math.abs(f(b.labeldistance, b.nametbdistance, 50));
                c.showLabelsAtCenter = f(b.showlabelsatcenter, 0);
                c.yScale = e >= 0 && e <= 40 ? e / 200 : 0.2;
                g.backgroundColor = {
                    FCcolor: {
                        color: m(b.bgcolor, x.get2DBgColor()),
                        alpha: m(b.bgalpha, x.get2DBgAlpha()),
                        angle: m(b.bgangle, x.get2DBgAngle()),
                        ratio: m(b.bgratio, x.get2DBgRatio())
                    }
                };
                g.borderColor = G(m(b.bordercolor, x.get2DBorderColor()), m(b.borderalpha, x.get2DBorderAlpha()));
                H.style.color = k.title.style.color = k.subtitle.style.color = aa(m(b.basefontcolor, x.get2DBaseFontColor()));
                H.connectorWidth = f(b.smartlinethickness, 1);
                H.connectorColor = G(m(b.smartlinecolor, x.get2DBaseFontColor()), f(b.smartlinealpha, 100));
                if (f(b.showlegend, 0)) k.legend.enabled = !0,
                k.legend.reversed = !Boolean(f(b.reverselegend, 0)),
                c.showInLegend = !0;
                if (!t && !A) k.plotOptions.series.dataLabels.enabled = !1,
                k.tooltip.enabled === !1 && (u = !1);
                c.useSameSlantAngle = f(b.usesameslantangle, ca ? 0 : 1);
                var M;
                for (e = g = 0; g < y; g += 1) if (h = a[g], !a[g].vline && (h.value = H = E.getCleanValue(h.value, !0), H !== null)) M = M || H,
                h.value = H,
                v.push(h),
                d += H,
                e += 1,
                M = Math.max(M, H);
                c.valueSum = d;
                y = v.length; ! w && ca && v.sort(function(b, a) {
                    return a.value - b.value
                }); ! w && !ca && (C += 1, c.data.push({
                    showInLegend: !1,
                    y: d,
                    name: "",
                    shadow: l,
                    smartTextObj: n,
                    color: j,
                    alpha: i,
                    borderColor: G(p, o),
                    borderWidth: s,
                    link: z(h.link),
                    displayValue: J
                }));
                for (g = 0; g < v.length; g += 1) if (h = v[g], a = h.value, y = g ? v[g - 1].value: a, e = Z(m(h.label, h.name, J)), n = B.getOriSize(e), l = g && !w && ca ? g - 1 : g, j = m(h.color, k.colors[l % k.colors.length]), i = m(h.alpha, b.plotfillalpha, V), p = m(h.bordercolor, F, R(j, 25)).split(q)[0], o = b.showplotborder != 1 ? ga: m(h.borderalpha, b.plotborderalpha, b.pieborderalpha, "80"), l = {
                    opacity: Math.max(i, o) / 100
                },
                c.data.push({
                    showInLegend: e !== J,
                    y: a,
                    name: e,
                    shadow: l,
                    smartTextObj: n,
                    color: j,
                    alpha: i,
                    borderColor: G(p, o),
                    borderWidth: s,
                    link: z(h.link)
                }), u) n = e,
                i = E.percentValue(a / (w || !ca ? d: L ? y: M) * 100),
                p = E.dataLabels(a) || J,
                j = r === 1 ? i: p,
                l = t === 1 ? n: J,
                i = f(h.showvalue, A) === 1 ? P === 1 ? i: p: J,
                i = (p = z(Z(h.displayvalue))) ? p: i !== J && l !== J ? l + I + i: m(l, i) || J,
                n = n != J ? n + D + j: j,
                c.data[C].displayValue = i,
                c.data[C].toolText = m(Z(h.tooltext), n),
                C += 1;
                c.labelMaxWidth = 0;
                return c
            },
            configureAxis: function(d) {
                var c = 0,
                a = d[K],
                b;
                delete a.x;
                delete a[0];
                delete a[1];
                d.chart.plotBorderColor = d.chart.plotBackgroundColor = O;
                a = a.pieDATALabels = [];
                if (d.series.length === 1 && (b = d.series[0].data) && (c = d.series[0].data.length) > 0 && d.plotOptions.series.dataLabels.enabled) for (; c--;) b[c] && z(b[c].displayValue) !== void 0 && a.push(b[c].displayValue)
            },
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel;
                a -= d.chart.marginRight + d.chart.marginLeft;
                var e = b - (d.chart.marginTop + d.chart.marginBottom);
                if (b = d.series[0]) {
                    fa(d, c, a, e / 2);
                    var e = b.data,
                    g = e.length,
                    h, l, j, i = 0,
                    c = b.labelDistance,
                    f = b.showLabelsAtCenter,
                    o, n = a * 0.7,
                    m = n - c,
                    v = a * 0.3;
                    h = d.plotOptions.series.dataLabels.style;
                    var s = parseInt(h.lineHeight),
                    u = e[0].y,
                    w,
                    r,
                    t = 0.8 / u,
                    A = b.useSameSlantAngle == 1,
                    P = !b.streamlinedData;
                    k.setStyle(h);
                    if (e[0].displayValue) l = k.getSmartText(e[0].displayValue, a, s),
                    e[0].displayValue = l.text,
                    e[0].labelWidht = k.getOriSize(l.text).width,
                    d.chart.marginTop += s + 10;
                    for (g -= 1; g; g -= 1) h = e[g],
                    f ? k.getSmartText(h.displayValue, a, s) : (w = P ? 0.2 + t * i: A ? h.y / u: Math.sqrt(h.y / u), r === void 0 && (r = w), o = v * w, j = m + (v - o) / 2, l = k.getSmartText(h.displayValue, j, s), h.displayValue = l.text, l = l.width > 0 ? j - l.width: j + c, l = 1 / (w + 1) * (o + 2 * l + v), n = Math.min(n, l - v), i += h.y);
                    k = v + n;
                    d.chart.marginRight += a - k;
                    b.connectorWidth = c;
                    b.is2d || (d.chart.marginTop += k * b.yScale / 2, d.chart.marginBottom += k * b.yScale * r / 2)
                }
            },
            useSortedData: !0,
            creditLabel: !1
        },
        ja);
        x("pyramid", {
            defaultGaugePaletteOptions: Y,
            subTitleFontSizeExtender: 0,
            drawAnnotations: !0,
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel;
                a -= d.chart.marginRight + d.chart.marginLeft;
                var e = d.series[0];
                fa(d, c, a, (b - (d.chart.marginTop + d.chart.marginBottom)) / 2);
                if (e) {
                    var b = e.data,
                    g = b.length,
                    h, l, j, i, f = 0,
                    c = e.labelDistance,
                    o, n, m = e.valueSum,
                    v = a * 0.7,
                    s = v - c,
                    u = a * 0.3,
                    w = u / m;
                    h = d.plotOptions.series.dataLabels.style;
                    h.lineHeight.split(/px/);
                    k.setStyle(h);
                    for (h = 0; h < g; h += 1) l = b[h],
                    o = f + l.y / 2,
                    n = o * w,
                    i = s + (u - n) / 2,
                    j = k.getSmartText(l.displayValue || J, i, 15),
                    l.displayValue = j.text,
                    j = j.width > 0 ? i - j.width: i + c,
                    o = (n + 2 * j + u) / (1 + o / m),
                    v = Math.min(v, o - u),
                    f += l.y;
                    k = u + v;
                    d.chart.marginRight += a - k;
                    d.chart.marginTop += 20;
                    e.connectorWidth = c;
                    e.is2d || (d.chart.marginBottom += k * e.yScale / 2)
                }
            },
            standaloneInit: !0,
            defaultSeriesType: "pyramid",
            defaultPlotShadow: 1,
            useSortedData: !1,
            isPyramid: 1,
            creditLabel: !1
        },
        x.funnel);
        var ya = function(d, c, a, b) {
            var k = d[K].smartLabel,
            e = c.chart,
            g = d.chart,
            h,
            l,
            j = 0,
            c = d.title,
            i = d.subtitle,
            p = c.text,
            o = i.text,
            n = d.series[0].captionPadding,
            m = 0,
            v = 0,
            s = j = 0,
            u = a * 0.7,
            a = f(e.captiononright, 0),
            w = z(e.captionposition, "top").toLowerCase(),
            e = {
                left: 0,
                right: 0
            };
            if (p !== J) h = c.style,
            m = f(parseInt(h.fontHeight, 10), parseInt(h.lineHeight, 10), 12),
            s = f(parseInt(h.fontSize, 10), 10);
            if (o !== J) l = i.style,
            v = f(parseInt(l.fontHeight, 10), parseInt(l.lineHeight, 10), 12),
            j = f(parseInt(l.fontSize, 10), 10);
            if (m > 0 || v > 0) {
                k.setStyle(h);
                h = k.getSmartText(c.text, u, m);
                k.setStyle(l);
                k = k.getSmartText(i.text, u, v);
                switch (w) {
                case "middle":
                    c.y = b / 2 + (s - (j > 0 ? j + 2 : 0)) / 2;
                    c.y = b / 2 - k.height / 2;
                    i.y = c.y + 2 + j;
                    break;
                case "bottom":
                    i.y = b - g.marginBottom - g.marginTop;
                    c.y = i.y - (k.height > 0 ? k.height + 2 : 0);
                    break;
                default:
                    c.y = s - 2,
                    i.y = h.height + j
                }
                j = Math.max(h.width, k.width);
                d.title.text = h.text;
                d.subtitle.text = k.text;
                j > 0 && (j += n);
                d = Math.min(j, u);
                a ? (c.align = i.align = Ka, e.right = d, c.x = h.width - j + n, i.x = k.width - j + n) : (c.align = i.align = La, e.left = d, c.x = j - h.width - n, i.x = j - k.width - n)
            }
            return e
        };
        x("sparkline", {
            standaloneInit: !0,
            defaultSeriesType: "sparkline",
            defaultPlotShadow: 1,
            useSortedData: !1,
            creditLabel: !1,
            subTitleFontSizeExtender: 0,
            subTitleFontWeight: "normal",
            drawAnnotations: !0,
            chartrightmargin: 3,
            chartleftmargin: 3,
            charttopmargin: 3,
            chartbottommargin: 3,
            defaultGaugePaletteOptions: S(Y, {
                paletteColors: [["555555", "A6A6A6", "CCCCCC", "E1E1E1", "F0F0F0"], ["A7AA95", "C4C6B7", "DEDFD7", "F2F2EE"], ["04C2E3", "66E7FD", "9CEFFE", "CEF8FF"], ["FA9101", "FEB654", "FED7A0", "FFEDD5"], ["FF2B60", "FF6C92", "FFB9CB", "FFE8EE"]],
                bgColor: ["FFFFFF", "CFD4BE,F3F5DD", "C5DADD,EDFBFE", "A86402,FDC16D", "FF7CA0,FFD1DD"],
                bgAngle: [270, 270, 270, 270, 270],
                bgRatio: ["0,100", "0,100", "0,100", "0,100", "0,100"],
                bgAlpha: ["100", "60,50", "40,20", "20,10", "30,30"],
                canvasBgColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
                canvasBgAngle: [0, 0, 0, 0, 0],
                canvasBgAlpha: ["100", "100", "100", "100", "100"],
                canvasBgRatio: ["", "", "", "", ""],
                canvasBorderColor: ["BCBCBC", "BEC5A7", "93ADBF", "C97901", "FF97B1"],
                toolTipBgColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
                toolTipBorderColor: ["545454", "545454", "415D6F", "845001", "68001B"],
                baseFontColor: ["333333", "60634E", "025B6A", "A15E01", "68001B"],
                trendColor: ["666666", "60634E", "415D6F", "845001", "68001B"],
                plotFillColor: ["666666", "A5AE84", "93ADBF", "C97901", "FF97B1"],
                borderColor: ["767575", "545454", "415D6F", "845001", "68001B"],
                borderAlpha: [50, 50, 50, 50, 50],
                periodColor: ["EEEEEE", "ECEEE6", "E6ECF0", "FFF4E6", "FFF2F5"],
                winColor: ["666666", "60634E", "025B6A", "A15E01", "FF97B1"],
                lossColor: ["CC0000", "CC0000", "CC0000", "CC0000", "CC0000"],
                drawColor: ["666666", "A5AE84", "93ADBF", "C97901", "FF97B1"],
                scorelessColor: ["FF0000", "FF0000", "FF0000", "FF0000", "FF0000"]
            }),
            series: function(d, c, a) {
                if (d.dataset && d.dataset[0] && d.dataset[0].data && d.dataset[0].data.length > 0) {
                    var b = {
                        data: [],
                        colorByPoint: !0
                    },
                    k = d.dataset[0].data,
                    e = d.chart,
                    g = c.chart,
                    h = c[K].colorM = new X(c.chart.paletteIndex, e.palettethemecolor, this);
                    c.legend.enabled = !1;
                    b.canvasLeftMargin = f(e.canvasleftmargin, -1);
                    b.canvasRightMargin = f(e.canvasrightmargin, -1);
                    b.captionPadding = f(e.captionpadding, 2);
                    g.backgroundColor = {
                        FCcolor: {
                            color: m(e.bgcolor, h.get2DBgColor()),
                            alpha: m(e.bgalpha, h.get2DBgAlpha()),
                            angle: m(e.bgangle, h.get2DBgAngle()),
                            ratio: m(e.bgratio, h.get2DBgRatio())
                        }
                    };
                    g.borderColor = z(e.showborder) == 1 ? G(m(e.bordercolor, h.get2DBorderColor()), m(e.borderalpha, h.get2DBorderAlpha())) : O;
                    c.plotOptions.series.dataLabels.style.color = c.title.style.color = c.subtitle.style.color = aa(m(e.basefontcolor, h.get2DBaseFontColor()));
                    g.plotBackgroundColor = O;
                    g.plotBorderColor = this.sparkColumn ? G(m(e.canvasbordercolor, h.get2DCanvasBorderColor()), f(e.showcanvasborder, 1) == 0 ? ga: m(e.canvasborderalpha, V)) : O;
                    g.plotBorderWidth = f(e.canvasborderthickness, 1);
                    b.baseFont = m(e.basefont, "Verdana");
                    b.baseFontSize = f(e.basefontsize, 10);
                    b.baseFontColor = m(e.basefontcolor, h.get2DBaseFontColor());
                    c.tooltip.enabled = f(e.showtooltip, 0) == 1;
                    c.series.push(this.point(a, b, k, d.chart, c));
                    this.configureAxis(c, d);
                    d.trendlines && ia(d.trendlines, c.yAxis, c[K], !1, this.isBar)
                }
            },
            configureAxis: function(d, c) {
                var a = d[K],
                b = c.chart,
                k,
                e,
                g,
                h,
                l,
                j;
                f(parseInt(b.yaxisvaluesstep, 10), parseInt(b.yaxisvaluestep, 10), 1);
                k = d.yAxis[0];
                e = a[0];
                g = b.yaxismaxvalue;
                h = b.yaxisminvalue;
                j = l = !f(this.isStacked ? 0 : this.setAdaptiveYMin, b.setadaptiveymin, 0);
                a = f(a.numdivlines, b.numdivlines, 4);
                Ra(k, e, g, h, l, j, a, b.adjustdiv !== ga);
                k.labels.enabled = !1;
                k.gridLineWidth = 0;
                k.alternateGridColor = O
            },
            point: function(d, c, a, b, k) {
                var e, g, h, l, j, i, p, d = a.length,
                o = k[K],
                n = o.axisGridManager,
                C = k.xAxis,
                v = k.chart.paletteIndex,
                o = o.x,
                s = /3d$/.test(k.chart.defaultSeriesType),
                u = this.isBar,
                s = m(b.showplotborder, s ? ga: la) === la ? s ? 1 : f(b.plotborderthickness, 1) : 0;
                f(b.plotborderalpha, b.plotfillalpha, 100);
                m(b.plotbordercolor, Pa.plotBorderColor[v]).split(q);
                var w = 0;
                f(b.use3dlighting, 1);
                var r = k[K].numberFormatter,
                t,
                A = f(b.plotborderdashed, 0),
                P = f(b.plotborderdashlen, 5),
                D = f(b.plotborderdashgap, 4),
                I = new X(v, b.palettethemecolor, this),
                v = !0,
                F,
                E,
                B,
                x,
                H,
                y,
                z,
                L,
                M,
                Na,
                N;
                c.canvasLeftMargin = f(b.canvasleftmargin, -1);
                x = m(b.linealpha, V);
                x = G(m(b.linecolor, I.get2DPlotFillColor()), x);
                e = f(b.linethickness, 1);
                g = f(b.drawanchors, b.showanchors, 0);
                y = f(b.anchorsides, 10);
                H = f(b.anchorradius, 2);
                B = m(b.anchoralpha, V);
                h = G(m(b.anchorcolor, I.get2DPlotFillColor()), B);
                j = Sa(y);
                c.openColor = aa(m(b.opencolor, "0099FF"));
                c.closeColor = aa(m(b.closecolor, "0099FF"));
                c.highColor = aa(m(b.highcolor, "00CC00"));
                c.lowColor = aa(m(b.lowcolor, "CC0000"));
                c.periodLength = f(b.periodlength, -1);
                c.periodColor = m(b.periodcolor, I.getPeriodColor());
                c.periodAlpha = m(b.periodalpha, V);
                L = f(b.showopenanchor, 1);
                M = f(b.showcloseanchor, 1);
                y = f(b.showhighanchor, 1);
                z = f(b.showlowanchor, 1);
                c.showOpenValue = f(b.showopenvalue, 1);
                c.showCloseValue = f(b.showclosevalue, 1);
                c.showHighLowValue = f(b.showhighlowvalue, 1);
                c.valuePadding = f(b.valuepadding, 2);
                Na = G(c.openColor, B);
                N = G(c.closeColor, B);
                I = G(c.highColor, B);
                B = G(c.lowColor, B);
                c.lineWidth = e;
                c.step = Boolean(f(b.drawstepline, 0));
                c.drawVerticalJoins = Boolean(f(b.drawverticaljoins, 1));
                c.openValue = r.getCleanValue(a && a[0] && a[0].value);
                c.closeValue = r.getCleanValue(a && a[d - 1] && a[d - 1].value);
                c.openValue = r.dataLabels(c.openValue);
                c.closeValue = r.dataLabels(c.closeValue);
                H = {
                    enabled: g == 1,
                    fillColor: h,
                    lineWidth: 0,
                    radius: H,
                    symbol: j
                };
                for (h = g = 0; g < d; g += 1) if (j = a[g], j.vline) n.addVline(C, j, w, k);
                else {
                    e = r.getCleanValue(j.value);
                    p = f(j.showlabel, b.showlabels, 1);
                    l = Z(!p ? J: Aa(j.label, j.name));
                    n.addXaxisCat(C, w, w, l);
                    w += 1;
                    e < 0 && (i = 360 - i);
                    p = {
                        opacity: NaN,
                        inverted: u
                    };
                    t = f(j.dashed, A) ? Ba(m(j.dashlen, P), m(j.dashgap, D), s) : void 0;
                    j = this.getPointStub(j, e, l, k);
                    if (f(b.showvalues, 0) == 0) j.displayValue = J;
                    c.data.push(T(j, {
                        y: e,
                        shadow: p,
                        color: x,
                        dashStyle: t,
                        marker: H
                    }));
                    typeof e != "undefined" && e != null && (v && (F = E = e, v = !1), F = Math.max(e, F), E = Math.min(e, E));
                    this.pointValueWatcher(k, e);
                    h += 1
                }
                c.maxDataValue = F;
                c.minDataValue = E;
                c.data[0].marker.enabled = L == 1;
                c.data[0].marker.fillColor = Na;
                c.data[d - 1].marker.enabled = M == 1;
                c.data[d - 1].marker.fillColor = N;
                for (g = 0; g < d; g += 1) {
                    j = c.data[g];
                    if (j.y == F) j.marker.enabled = y == 1,
                    j.marker.fillColor = I;
                    if (j.y == E) j.marker.enabled = z == 1,
                    j.marker.fillColor = B
                }
                o.catCount = w;
                c.shadow = f(b.showshadow, 0);
                return c
            },
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel,
                e = d.series[0],
                g = d.chart,
                h = a - (g.marginRight + g.marginLeft),
                l = b - (g.marginTop + g.marginBottom),
                j;
                if (e) {
                    j = e.valuePadding;
                    var h = ya(d, c, a, b),
                    i = 0,
                    b = 0,
                    p = e.openValue,
                    o = e.closeValue;
                    typeof p != Da && (i = k.getSmartText(p).width + j);
                    typeof o != Da && (b = k.getSmartText(o).width + j);
                    var p = 0,
                    o = e.maxDataValue,
                    n = e.minDataValue,
                    m = d.plotOptions.series.dataLabels.style;
                    f(parseInt(m.fontHeight, 10), parseInt(m.lineHeight, 10), 12);
                    f(parseInt(m.fontSize, 10), 10);
                    k.setStyle(m);
                    typeof o != Da && (p = k.getSmartText("[" + o + "|" + n + "]").width + j);
                    g.marginRight += b + p;
                    g.marginLeft += h.left + i;
                    g.marginRight += h.right;
                    if (e.canvasLeftMargin != -1) g.marginLeft = e.canvasLeftMargin;
                    if (e.canvasRightMargin != -1) g.marginRight = e.canvasRightMargin + h.right;
                    h = a - (g.marginLeft + g.marginRight);
                    e.openValueX = -j;
                    e.closeValueX = h + j;
                    e.highLowValueX = h + b + j;
                    e.dataLabelY = g.marginTop + l / 2;
                    e.dataLabelY = g.marginTop + l / 2;
                    this.xAxisMinMaxSetter(d, c, h)
                }
            }
        },
        ja);
        x("sparkcolumn", {
            standaloneInit: !0,
            defaultSeriesType: "column",
            defaultPlotShadow: 1,
            useSortedData: !1,
            isPyramid: 1,
            creditLabel: !1,
            sparkColumn: !0,
            drawAnnotations: !0,
            defaultGaugePaletteOptions: x.sparkline.defaultGaugePaletteOptions,
            chartbottommargin: 3,
            charttopmargin: 3,
            chartrightmargin: 3,
            chartleftmargin: 3,
            point: function(d, c, a, b, k) {
                var e = k.chart,
                g, h, l, j, i, p, d = a.length,
                o = k[K],
                n = o.axisGridManager,
                C = k.xAxis,
                v = k.chart.paletteIndex,
                o = o.x,
                s = /3d$/.test(k.chart.defaultSeriesType),
                s = m(b.showplotborder, s ? ga: la) === la ? s ? 1 : f(b.plotborderthickness, 1) : 0,
                u = 0,
                w = Boolean(f(b.use3dlighting, 1)),
                r = k[K].numberFormatter,
                t = f(b.plotborderdashed, 0),
                A = f(b.plotborderdashlen, 5),
                P = f(b.plotborderdashgap, 4);
                g = new X(v, b.palettethemecolor, this);
                var D, v = !0,
                q, F, E;
                c.canvasLeftMargin = f(b.canvasleftmargin, -1);
                e.plotBackgroundColor = {
                    FCcolor: {
                        color: m(b.canvasbgcolor, g.get2DCanvasBgColor()),
                        alpha: m(b.canvasbgalpha, g.get2DCanvasBgAlpha()),
                        angle: m(b.canvasbgangle, g.get2DCanvasBgAngle()),
                        ratio: m(b.canvasbgratio, g.get2DCanvasBgRatio())
                    }
                };
                c.plotSpacePercent = f(b.plotspacepercent, 10);
                if (c.plotSpacePercent < 0 || c.plotSpacePercent > 80) c.plotSpacePercent = 10;
                D = m(b.plotfillalpha, V);
                e = G(m(b.plotfillcolor, g.get2DPlotFillColor()), D);
                E = G(m(b.highcolor, b.plotfillcolor), D);
                D = G(m(b.lowcolor, b.plotfillcolor), D);
                c.periodLength = f(b.periodlength, -1);
                c.periodColor = m(b.periodcolor, g.getPeriodColor());
                c.periodAlpha = f(b.periodalpha, V);
                c.openValue = r.getCleanValue(a && a[0] && a[0].value);
                c.closeValue = r.getCleanValue(a && a[d - 1] && a[d - 1].value);
                c.openValue = r.dataLabels(c.openValue);
                c.closeValue = r.dataLabels(c.closeValue);
                for (l = h = 0; h < d; h += 1) if (i = a[h], i.vline) n.addVline(C, i, u, k);
                else {
                    g = r.getCleanValue(i.value);
                    p = f(i.showlabel, b.showlabels, 1);
                    j = Z(!p ? J: Aa(i.label, i.name));
                    n.addXaxisCat(C, u, u, j);
                    u += 1;
                    p = f(i.dashed, t) ? Ba(m(i.dashlen, A), m(i.dashgap, P), s) : void 0;
                    i = this.getPointStub(i, g, j, k);
                    if (f(b.showvalues, 0) == 0) i.displayValue = J;
                    c.data.push(T(i, {
                        y: g,
                        shadow: void 0,
                        color: e,
                        borderColor: e,
                        borderWidth: s,
                        use3DLighting: w,
                        dashStyle: p
                    }));
                    typeof g != "undefined" && g != null && (v && (q = F = g, v = !1), q = Math.max(g, q), F = Math.min(g, F));
                    this.pointValueWatcher(k, g);
                    l += 1
                }
                for (h = 0; h < d; h += 1) {
                    i = c.data[h];
                    if (i.y == q) i.color = E,
                    i.borderColor = E;
                    if (i.y == F) i.color = D,
                    i.borderColor = D
                }
                o.catCount = u;
                c.shadow = f(b.showshadow, 0);
                return c
            },
            spaceManager: function(d, c, a, b) {
                var k = d.series[0],
                e = d.chart,
                g = a - (e.marginRight + e.marginLeft);
                if (k) {
                    b = ya(d, c, a, b);
                    e.marginLeft += b.left;
                    e.marginRight += b.right;
                    if (k.canvasLeftMargin != -1) e.marginLeft = k.canvasLeftMargin;
                    if (k.canvasRightMargin != -1) e.marginRight = k.canvasRightMargin + e.marginRight;
                    g = a - e.marginLeft - e.marginRight;
                    this.xAxisMinMaxSetter(d, c, g)
                }
            }
        },
        x.sparkline);
        x("sparkwinloss", {
            standaloneInit: !0,
            defaultSeriesType: "sparkwinloss",
            defaultPlotShadow: 1,
            useSortedData: !1,
            isPyramid: 1,
            creditLabel: !1,
            drawAnnotations: !0,
            defaultGaugePaletteOptions: x.sparkline.defaultGaugePaletteOptions,
            point: function(d, c, a, b, k) {
                var e, g, h, l, j, i, d = a.length,
                p = k[K],
                o = p.axisGridManager,
                n = k.xAxis,
                C = k.chart.paletteIndex,
                p = p.x,
                v = /3d$/.test(k.chart.defaultSeriesType),
                v = m(b.showplotborder, v ? ga: la) === la ? v ? 1 : f(b.plotborderthickness, 1) : 0,
                s = 0,
                u = Boolean(f(b.use3dlighting, 1)),
                w = f(b.plotborderdashed, 0),
                r = f(b.plotborderdashlen, 5),
                t = f(b.plotborderdashgap, 4);
                e = new X(C, b.palettethemecolor, this);
                var A, q = !0,
                D = 0,
                I = 0,
                F = 0,
                E, B, x, H;
                c.canvasLeftMargin = f(b.canvasleftmargin, -1);
                c.plotSpacePercent = f(b.plotspacepercent, 10);
                if (c.plotSpacePercent < 0 || c.plotSpacePercent > 80) c.plotSpacePercent = 10;
                A = m(b.plotfillalpha, V);
                C = G(m(b.plotfillcolor, e.get2DPlotFillColor()), A);
                E = G(m(b.wincolor, e.getWinColor()), A);
                B = G(m(b.lossColor, e.getLossColor()), A);
                x = G(m(b.drawcolor, e.getDrawColor()), A);
                A = G(m(b.scorelesscolor, e.getScoreLessColor()), A);
                c.periodLength = f(b.periodlength, -1);
                c.periodColor = m(b.periodcolor, e.getPeriodColor());
                c.periodAlpha = f(b.periodalpha, V);
                c.valuePadding = f(b.valuepadding, 2);
                for (h = g = 0; g < d; g += 1) if (j = a[g], j.vline) o.addVline(n, j, s, k);
                else {
                    e = j.value.toLowerCase();
                    switch (e) {
                    case "w":
                        e = 1;
                        D += 1;
                        H = E;
                        break;
                    case "l":
                        e = -1;
                        I += 1;
                        H = B;
                        break;
                    case "d":
                        e = 0.1;
                        F += 1;
                        H = x;
                        break;
                    default:
                        e = null,
                        H = C
                    }
                    j.scoreless == 1 && (H = A);
                    i = f(j.showlabel, b.showlabels, 1);
                    l = Z(!i ? J: Aa(j.label, j.name));
                    o.addXaxisCat(n, s, s, l);
                    s += 1;
                    i = f(j.dashed, w) ? Ba(m(j.dashlen, r), m(j.dashgap, t), v) : void 0;
                    j = this.getPointStub(j, e, l, k);
                    if (f(b.showvalues, 0) == 0) j.displayValue = J;
                    k.tooltip.enabled = !1;
                    c.data.push(T(j, {
                        y: e,
                        shadow: void 0,
                        color: H,
                        borderColor: H,
                        borderWidth: v,
                        use3DLighting: u,
                        dashStyle: i
                    }));
                    typeof e != "undefined" && e != null && q && (q = !1);
                    this.pointValueWatcher(k, e);
                    h += 1
                }
                c.labelText = D + "-" + I + "-" + F;
                p.catCount = s;
                c.shadow = f(b.showshadow, 0);
                return c
            },
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel,
                e = d.series[0],
                g = d.chart,
                h = a - (g.marginRight + g.marginLeft),
                l = b - g.marginTop - g.marginBottom,
                j;
                if (e) {
                    j = e.valuePadding || 0;
                    b = ya(d, c, a, b);
                    k = k.getSmartText(e.labelText).width + j;
                    g.marginLeft += b.left;
                    g.marginRight += b.right + k + 15;
                    if (e.canvasLeftMargin != -1) g.marginLeft = e.canvasLeftMargin;
                    if (e.canvasRightMargin != -1) g.marginRight = e.canvasRightMargin;
                    h = a - g.marginLeft - g.marginRight;
                    e.dataLabelX = h + j;
                    e.dataLabelY = l / 2 + g.marginTop;
                    this.xAxisMinMaxSetter(d, c, h)
                }
            },
            xAxisMinMaxSetter: function(d) {
                var c = d[K].x,
                a = c.min = f(c.min, 0),
                c = c.max = f(c.max, c.catCount - 1),
                d = d.xAxis;
                d.labels.enabled = !1;
                d.gridLineWidth = 0;
                d.alternateGridColor = O;
                d.min = a - 0.5;
                d.max = c + 0.5
            },
            configureAxis: function(d, c) {
                var a = d[K],
                b = c.chart,
                k,
                e;
                f(parseInt(b.yaxisvaluesstep, 10), parseInt(b.yaxisvaluestep, 10), 1);
                k = d.yAxis[0];
                e = a[0];
                f(this.isStacked ? 0 : this.setAdaptiveYMin, b.setadaptiveymin, 0);
                f(a.numdivlines, b.numdivlines, 4);
                k.min = -1;
                k.max = 1;
                k.tickInterval = 1;
                delete e.max;
                delete e.min;
                k.labels.enabled = !1;
                k.gridLineWidth = 0;
                k.alternateGridColor = O
            }
        },
        x.sparkline);
        x("hlineargauge", {
            creditLabel: !1,
            defaultSeriesType: "lineargauge",
            defaultPlotShadow: 1,
            standaloneInit: !0,
            isHorizontal: !0,
            drawAnnotations: !0,
            defaultGaugePaletteOptions: Y,
            subTitleFontSizeExtender: 0,
            realtimeEnabled: !0,
            eiMethods: {
                feedData: function() {
                    console.warn("data fed: ", arguments)
                }
            },
            linearDataParser: x.angulargauge.linearDataParser,
            series: function(d, c) {
                var a = {
                    data: [],
                    colorByPoint: !0
                },
                b = d.chart,
                k = c.chart,
                e = c[K],
                g = this.numberFormatter = e.numberFormatter,
                h = T({},
                d.colorrange),
                l = new da(h.color, void 0, c.chart.paletteIndex),
                j = new X(c.chart.paletteIndex, b.palettethemecolor, this),
                h = g.getCleanValue(d.value) || 0;
                f(b.showvalue, 1) && g.dataLabels(h);
                var i = l.getColorObj(h),
                p = i.isOnMeetPoint ? i.nextObj: i.colorObj,
                h = f(b.showvalue, b.showrealtimevalue, 1),
                o = f(b.placevaluesinside, 0);
                a.tickValueStyle = c.yAxis[0].labels.style;
                this.colorM = j;
                k.backgroundColor = {
                    FCcolor: {
                        color: m(b.bgcolor, j.get2DBgColor()),
                        alpha: m(b.bgalpha, j.get2DBgAlpha()),
                        angle: m(b.bgangle, j.get2DBgAngle()),
                        ratio: m(b.bgratio, j.get2DBgRatio())
                    }
                };
                k.borderColor = G(m(b.bordercolor, j.get2DBorderColor()), m(b.borderalpha, j.get2DBorderAlpha()));
                c.plotOptions.series.dataLabels.style.color = c.title.style.color = c.subtitle.style.color = a.tickValueStyle.color = aa(m(b.basefontcolor, j.get2DBaseFontColor()));
                c.plotOptions.series.dataLabels.style.fontWeight = "bold";
                k = f(this.isPyramid ? b.pyramidyscale: b.funnelyscale);
                a.yScale = k >= 0 && k <= 40 ? k / 200 : 0.2;
                p || (p = i.nextObj || i.prevObj);
                a.colorRangeManager = l;
                Z(p.label);
                a.setAdaptiveMin = f(b.setadaptivemin, 0);
                a.upperLimit = z(b.upperlimit);
                a.lowerLimit = z(b.lowerlimit);
                a.upperLimitDisplay = z(b.upperlimitdisplay);
                a.lowerLimitDisplay = z(b.lowerlimitdisplay);
                a.autoScale = f(b.autoscale, 1);
                a.origW = z(b.origw);
                a.origH = z(b.origh);
                a.annRenderDelay = z(b.annrenderdelay);
                a.ticksOnRight = f(b.ticksonright, 1);
                a.ticksBelowGauge = f(b.ticksbelowgauge, 1);
                a.pointerOnTop = f(b.pointerontop, 0);
                a.pointerOnOpp = f(b.pointeronopp, typeof b.pointerontop === "undefined" ? void 0 : a.pointerOnTop ? 0 : 1, a.ticksBelowGauge ? 0 : 1);
                a.valueAbovePointer = f(b.valueabovepointer, a.pointerOnOpp ? 0 : 1);
                a.valueInsideGauge = f(b.valueinsidegauge, a.pointerOnOpp && a.valueAbovePointer || !a.pointerOnOpp && !a.valueAbovePointer ? 1 : 0);
                a.showTickMarks = f(b.showtickmarks, 1);
                a.showTickValues = f(b.showtickvalues, a.showTickMarks);
                a.showLimits = f(b.showlimits, a.showTickValues);
                a.showShadow = f(b.showshadow, 1);
                a.showPointerShadow = f(b.showpointershadow, a.showShadow);
                a.adjustTM = f(b.adjusttm, 1);
                a.majorTMNumber = f(b.majortmnumber, -1);
                a.majorTMColor = m(b.majortmcolor, j.getTickColor());
                a.majorTMAlpha = f(b.majortmalpha, 100);
                a.majorTMHeight = f(b.majortmheight, b.majortmwidth, 6);
                a.majorTMThickness = f(b.majortmthickness, 1);
                a.minorTMNumber = f(b.minortmnumber, 4);
                a.minorTMColor = m(b.minortmcolor, a.majorTMColor);
                a.minorTMAlpha = f(b.minortmalpha, a.majorTMAlpha);
                a.minorTMHeight = f(b.minortmheight, b.minortmwidth, Math.round(a.majorTMHeight / 2));
                a.minorTMThickness = f(b.minortmthickness, 1);
                a.tickMarkDistance = f(b.tickmarkdistance, b.tickmarkgap, 3);
                a.tickValueDistance = f(b.tickvaluedistance, b.displayvaluedistance, 3);
                a.tickValueStep = f(b.tickvaluestep, b.tickvaluesstep, 1);
                a.tickValueStep = a.tickValueStep < 1 ? 1 : a.tickValueStep;
                a.connectTickMarks = f(b.connecttickmarks, 1);
                a.useMessageLog = f(b.usemessagelog, 0);
                a.messageLogWPercent = f(b.messagelogwpercent, 80);
                a.messageLogHPercent = f(b.messageloghpercent, 70);
                a.messageLogShowTitle = f(b.messagelogshowtitle, 1);
                a.messageLogTitle = m(b.messagelogtitle, "Message Log");
                a.messageLogColor = m(b.messagelogcolor, j.get2DMsgLogColor());
                a.messageGoesToLog = f(b.messagegoestolog, 1);
                a.messageGoesToJS = f(b.messagegoestojs, 0);
                a.messageJSHandler = m(b.messagejshandler, "alert");
                a.messagePassAllToJS = f(b.messagepassalltojs, 0);
                a.showValue = f(b.showvalue, b.showrealtimevalue, 1);
                a.valuePadding = f(b.valuepadding, 4);
                a.dataStreamURL = unescape(m(b.datastreamurl, ""));
                a.streamURLQMarkPresent = a.dataStreamURL.indexOf("?") != -1;
                a.refreshInterval = f(b.refreshinterval, 1);
                a.updateInterval = f(b.updateinterval, a.refreshInterval);
                a.dataStamp = m(b.datastamp, "");
                a.gaugeType = f(b.gaugetype, 1);
                if (a.gaugeType > 2) this.isHorizontal = !1;
                a.reverseScale = a.gaugeType === 2 || a.gaugeType === 3 ? 1 : 0;
                a.gaugeOriginX = f(b.gaugeoriginx, b.ledoriginx, -1);
                a.gaugeOriginY = f(b.gaugeoriginy, b.ledoriginy, -1);
                a.gaugeHeight = f(b.gaugeheight, b.ledheight, -1);
                a.gaugeWidth = f(b.gaugewidth, b.ledwidth, -1);
                a.showGaugeLabels = f(b.showgaugelabels, b.showcolornames, 1);
                a.gaugeFillMix = b.gaugefillmix;
                a.gaugeFillRatio = b.gaugefillratio;
                if (a.gaugeFillMix == void 0) a.gaugeFillMix = "{light-10},{dark-20},{light-50},{light-85}";
                if (a.gaugeFillRatio == void 0) a.gaugeFillRatio = "0,8,84,8";
                a.showGaugeBorder = f(b.showgaugeborder, 1);
                a.gaugeBorderColor = m(b.gaugebordercolor, "{dark-20}");
                a.gaugeBorderThickness = f(b.gaugeborderthickness, 1);
                a.gaugeBorderAlpha = f(b.gaugeborderalpha, 100);
                a.gaugeRoundRadius = f(b.gaugeroundradius, 0);
                var k = f(b.pointerradius, 10),
                l = m(b.pointerbgcolor, b.pointercolor, j.getPointerBgColor()),
                i = f(b.pointerbgalpha, 100),
                n = m(b.pointerbordercolor, j.getPointerBorderColor()),
                C = f(b.pointerborderthickness, 1),
                v = f(b.pointerborderalpha, 100),
                s = f(b.pointersides, 3),
                u,
                w,
                r,
                p = 0,
                t,
                A;
                u = d.pointers && d.pointers.pointer || [];
                j = p = u[1] && u[1].value || 0;
                w = 0;
                for (r = u.length; w < r; w += 1) t = u[w],
                A = g.getCleanValue(t.value),
                t.id = m(t.id, "pointer_" + w),
                t.showvalue = f(t.showvalue, b.showvalue),
                t.editable = m(t.editable, b.editmode),
                t.borderColor = G(m(t.bordercolor, n), m(t.borderalpha, v)),
                t.borderWidth = m(t.borderthickness, C),
                t.color = G(m(t.color, t.bgcolor, l), m(t.alpha, t.bgalpha, i)),
                t.radius = m(t.radius, k),
                t.sides = f(t.sides, s),
                t.displayvalue = g.dataLabels(A),
                t.doNotSlice = !0,
                p = Math.max(A, p),
                j = Math.min(A, j),
                a.data.push(T(this.getPointStub(t, A, "", c), t));
                e = new ma(b.lowerlimit, b.upperlimit, !1, a, e.numberFormatter);
                e.calculateLimits(p, j);
                e.calculateTicks();
                a.majorTM = e.getMajorTM();
                a.minorTM = e.getMinorTM();
                a.min = e.min;
                a.max = e.max;
                e = f(b.is3d, 1);
                f(b.usecolornameasvalue, 0);
                a.is3D = e;
                a.placeValuesInside = o;
                a.showValue = h;
                c.legend.enabled = !1;
                HCData = a.data;
                c.chart.plotBackgroundColor = O;
                c.chart.plotBorderColor = O;
                a instanceof Array ? c.series = c.series.concat(a) : c.series.push(a);
                this.configureAxis(c, d);
                d.trendlines && ia(d.trendlines, c.yAxis, c[K], !1, this.isBar)
            },
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel,
                e = d.series[0],
                g = d.chart,
                h,
                l,
                j = a - (g.marginRight + g.marginLeft),
                b = h = b - (g.marginTop + g.marginBottom),
                i = g.marginLeft,
                f = g.marginTop,
                o = e.valuePadding,
                n = b * 0.7 - o,
                g = a - (g.marginRight + i),
                m = 0,
                v = l = 0,
                s = 0,
                u = 0,
                w = e.gaugeWidth,
                r = e.gaugeHeight,
                t = e.gaugeOriginX,
                A = e.gaugeOriginY;
                e.dataLabels = {
                    style: d.plotOptions.series.dataLabels.style
                };
                a = e.dataLabels.style;
                k.setStyle(a);
                l = e.autoScale ? ha(e.origW, e.origH, j, b) : 1;
                e.scaleFactor = l;
                c = fa(d, c, j, h / 2);
                b -= c;
                l = pa(d, j, b, this.isHorizontal);
                var q, D, I;
                if ((q = e.data) && !e.valueInsideGauge) {
                    h = 0;
                    for (I = q.length; h < I; h += 1) D = q[h],
                    D.showvalue != 0 && D.displayValue != J && (d = k.getSmartText(D.displayvalue, g, n, !0), s = Math.max(o + d.height + D.radius * 1, s), v = Math.max(o + d.width + D.radius * 1, v))
                }
                this.isHorizontal ? (b -= e.ticksBelowGauge == e.pointerOnOpp ? Math.max(l, s) : l + s, e.ticksBelowGauge == 0 && (u = l), f += e.pointerOnOpp ? u: Math.max(u, s)) : (j -= e.ticksOnRight == e.pointerOnOpp ? Math.max(l, v) : l + v, e.ticksOnRight == 0 && (m = l), i += e.pointerOnOpp ? m: Math.max(m, v));
                e.valuePadding = o;
                e.labelLineHeight = a.lineHeight.split(/px/)[0];
                e.gaugeWidth = w == -1 ? Math.max(j, 10) : w;
                e.gaugeHeight = r == -1 ? Math.max(b, 10) : r;
                e.gaugeOriginX = t == -1 ? i: t;
                e.gaugeOriginY = A == -1 ? f + c: A
            }
        },
        ja);
        x("bullet", {
            creditLabel: !1,
            defaultSeriesType: "bullet",
            defaultPlotShadow: 1,
            drawAnnotations: !0,
            realtimeEnabled: !1,
            defaultGaugePaletteOptions: S(Y, {
                paletteColors: [["A6A6A6", "CCCCCC", "E1E1E1", "F0F0F0"], ["A7AA95", "C4C6B7", "DEDFD7", "F2F2EE"], ["04C2E3", "66E7FD", "9CEFFE", "CEF8FF"], ["FA9101", "FEB654", "FED7A0", "FFEDD5"], ["FF2B60", "FF6C92", "FFB9CB", "FFE8EE"]],
                bgColor: ["FFFFFF", "CFD4BE,F3F5DD", "C5DADD,EDFBFE", "A86402,FDC16D", "FF7CA0,FFD1DD"],
                bgAngle: [270, 270, 270, 270, 270],
                bgRatio: ["0,100", "0,100", "0,100", "0,100", "0,100"],
                bgAlpha: ["100", "60,50", "40,20", "20,10", "30,30"],
                toolTipBgColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
                toolTipBorderColor: ["545454", "545454", "415D6F", "845001", "68001B"],
                baseFontColor: ["333333", "60634E", "025B6A", "A15E01", "68001B"],
                tickColor: ["333333", "60634E", "025B6A", "A15E01", "68001B"],
                trendColor: ["545454", "60634E", "415D6F", "845001", "68001B"],
                plotFillColor: ["545454", "60634E", "415D6F", "845001", "68001B"],
                borderColor: ["767575", "545454", "415D6F", "845001", "68001B"],
                borderAlpha: [50, 50, 50, 50, 50]
            }),
            subTitleFontSizeExtender: 0,
            subTitleFontWeight: "normal",
            series: function(d, c) {
                var a = {
                    data: [],
                    colorByPoint: !0
                },
                b = d.chart,
                k,
                e = c.chart,
                g = c[K],
                h = this.numberFormatter = g.numberFormatter,
                l = T({},
                d.colorrange),
                j = new da(l.color, void 0, this.defaultGaugePaletteOptions.paletteColors[c.chart.paletteIndex]),
                i = new X(c.chart.paletteIndex, b.palettethemecolor, this),
                l = h.getCleanValue(d.value) || 0;
                f(b.showvalue, 1) && h.dataLabels(l);
                var p = j.getColorObj(l),
                o = p.isOnMeetPoint ? p.nextObj: p.colorObj,
                l = f(b.showvalue, b.showrealtimevalue, 1),
                n = f(b.placevaluesinside, 0);
                a.tickValueStyle = c.yAxis[0].labels.style;
                this.colorM = i;
                k = f(this.isPyramid ? b.pyramidyscale: b.funnelyscale);
                a.yScale = k >= 0 && k <= 40 ? k / 200 : 0.2;
                o || (o = p.nextObj || p.prevObj);
                a.colorRangeManager = j;
                Z(o.label);
                e.backgroundColor = {
                    FCcolor: {
                        color: m(b.bgcolor, i.get2DBgColor()),
                        alpha: m(b.bgalpha, i.get2DBgAlpha()),
                        angle: m(b.bgangle, i.get2DBgAngle()),
                        ratio: m(b.bgratio, i.get2DBgRatio())
                    }
                };
                e.borderColor = G(m(b.bordercolor, i.get2DBorderColor()), m(b.borderalpha, i.get2DBorderAlpha()));
                c.plotOptions.series.dataLabels.style.color = c.title.style.color = c.subtitle.style.color = a.tickValueStyle.color = aa(m(b.basefontcolor, i.get2DBaseFontColor()));
                c.plotOptions.series.dataLabels.style.fontWeight = "bold";
                a.setAdaptiveMin = f(b.setadaptivemin, 0);
                a.upperLimit = z(b.upperlimit);
                a.lowerLimit = z(b.lowerlimit);
                a.upperLimitDisplay = z(b.upperlimitdisplay);
                a.lowerLimitDisplay = z(b.lowerlimitdisplay);
                a.autoScale = f(b.autoscale, 1);
                a.origW = z(b.origw);
                a.origH = z(b.origh);
                a.annRenderDelay = z(b.annrenderdelay);
                a.captionPadding = f(b.captionpadding, 5);
                this.isHorizontal ? a.ticksBelowGauge = f(b.ticksbelowgauge, b.ticksbelowgraph, 1) : a.ticksOnRight = f(b.ticksonright, 0);
                a.showTickMarks = f(b.showtickmarks, 1);
                a.showTickValues = f(b.showtickvalues, a.showTickMarks);
                a.showLimits = f(b.showlimits, a.showTickValues);
                a.adjustTM = f(b.adjusttm, 1);
                a.majorTMNumber = f(b.majortmnumber, -1);
                a.majorTMColor = m(b.majortmcolor, i.getTickColor());
                a.majorTMAlpha = f(b.majortmalpha, 100);
                a.majorTMHeight = f(b.majortmheight, b.majortmwidth, 4);
                a.majorTMThickness = f(b.majortmthickness, 1);
                a.minorTMNumber = f(b.minortmnumber, 4);
                a.minorTMColor = m(b.minortmcolor, a.majorTMColor);
                a.minorTMAlpha = f(b.minortmalpha, 0);
                a.minorTMHeight = f(b.minortmheight, b.minortmwidth, Math.round(a.majorTMHeight / 2));
                a.minorTMThickness = f(b.minortmthickness, 1);
                a.tickMarkDistance = f(b.tickmarkdistance, b.tickmarkgap, 3);
                a.tickValueDistance = f(b.tickvaluedistance, b.displayvaluedistance, 3);
                a.tickValueStep = f(b.tickvaluestep, b.tickvaluesstep, 1);
                a.tickValueStep = a.tickValueStep < 1 ? 1 : a.tickValueStep;
                a.connectTickMarks = f(b.connecttickmarks, 0);
                a.useMessageLog = f(b.usemessagelog, 0);
                a.messageLogWPercent = f(b.messagelogwpercent, 80);
                a.messageLogHPercent = f(b.messageloghpercent, 70);
                a.messageLogShowTitle = f(b.messagelogshowtitle, 1);
                a.messageLogTitle = m(b.messagelogtitle, "Message Log");
                a.messageLogColor = m(b.messagelogcolor, i.get2DMsgLogColor());
                a.messageGoesToLog = f(b.messagegoestolog, 1);
                a.messageGoesToJS = f(b.messagegoestojs, 0);
                a.messageJSHandler = m(b.messagejshandler, "alert");
                a.messagePassAllToJS = f(b.messagepassalltojs, 0);
                a.showValue = f(b.showvalue, b.showrealtimevalue, 1);
                a.valuePadding = f(b.valuepadding, 4);
                a.dataStreamURL = unescape(m(b.datastreamurl, ""));
                a.streamURLQMarkPresent = a.dataStreamURL.indexOf("?") != -1;
                a.refreshInterval = f(b.refreshinterval, 1);
                a.dataStamp = m(b.datastamp, "");
                a.gaugeType = this.isHorizontal ? f(b.gaugetype, 1) : f(b.gaugetype, 4);
                a.gaugeOriginX = f(b.gaugeoriginx, b.ledoriginx, -1);
                a.gaugeOriginY = f(b.gaugeoriginy, b.ledoriginy, -1);
                a.gaugeHeight = f(b.gaugeheight, b.ledheight, -1);
                a.gaugeWidth = f(b.gaugewidth, b.ledwidth, -1);
                a.showGaugeLabels = f(b.showgaugelabels, b.showcolornames, 1);
                a.gaugeFillMix = b.colorrangefillmix;
                a.gaugeFillRatio = b.colorrangefillratio;
                if (a.gaugeFillMix == void 0) a.gaugeFillMix = "{light-10},{dark-20},{light-50},{light-85}";
                if (a.gaugeFillRatio == void 0) a.gaugeFillRatio = "0,8,84,8";
                a.showGaugeBorder = f(b.showcolorrangeborder, 0);
                a.showShadow = f(b.showshadow, 1);
                a.gaugeBorderColor = m(b.colorrangebordercolor, "{dark-20}");
                a.gaugeBorderThickness = f(b.colorrangeborderthickness, 1);
                a.gaugeBorderAlpha = f(b.colorrangeborderalpha, 100);
                a.gaugeRoundRadius = f(b.gaugeroundradius, 0);
                e = h.getCleanValue(d.value);
                j = h.getCleanValue(d.target);
                k = {
                    plotasdot: f(b.plotasdot, 0),
                    plotfillpercent: m(b.plotfillpercent, 40),
                    color: G(m(b.plotfillcolor, i.get2DPlotFillColor()), f(b.plotfillalpha, 100)),
                    showplotborder: f(b.showplotborder, 1),
                    borderColor: G(m(b.plotbordercolor, i.get2DPlotBorderColor()), f(b.plotborderalpha, 100)),
                    borderWidth: f(b.plotborderthickness, 1),
                    isValueUndef: d.value === void 0 ? !0 : !1,
                    value: e,
                    displayvalue: d.value === void 0 ? J: h.dataLabels(e),
                    doNotSlice: !0
                };
                a.data.push(T(this.getPointStub(k, e, "", c), k));
                h = {
                    targetfillpercent: m(b.targetfillpercent, 60),
                    color: aa(m(b.targetcolor, i.get2DPlotFillColor())),
                    borderWidth: 0,
                    targetthickness: f(b.targetthickness, 3),
                    isValueUndef: d.target === void 0 ? !0 : !1,
                    value: j,
                    displayvalue: d.target === void 0 ? J: h.dataLabels(j),
                    doNotSlice: !0
                };
                a.data.push(T(this.getPointStub(h, j, "", c), h)); ! isNaN(e) && !isNaN(j) ? (h = Math.max(e, j), i = Math.min(e, j)) : h = i = e || j;
                g = new ma(b.lowerlimit, b.upperlimit, !1, a, g.numberFormatter);
                g.calculateLimits(h, i);
                g.calculateTicks();
                a.majorTM = g.getMajorTM();
                a.minorTM = g.getMinorTM();
                a.min = g.min;
                a.max = g.max;
                a.useColorNameAsValue = f(b.usecolornameasvalue, 0);
                a.is3D = f(b.is3d, 1);
                a.placeValuesInside = n;
                a.showValue = l;
                c.legend.enabled = !1;
                HCData = a.data;
                c.chart.plotBackgroundColor = O;
                c.chart.plotBorderColor = O;
                a instanceof Array ? c.series = c.series.concat(a) : c.series.push(a);
                this.configureAxis(c, d);
                d.trendlines && ia(d.trendlines, c.yAxis, c[K], !1, this.isBar)
            }
        },
        x.hlineargauge);
        x("hbullet", {
            creditLabel: !1,
            defaultSeriesType: "hbullet",
            defaultPlotShadow: 1,
            isHorizontal: !0,
            standaloneInit: !0,
            subTitleFontSizeExtender: 0,
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel,
                e = d.series[0],
                g;
                g = d.chart;
                var h, l = g.marginRight,
                j = g.marginLeft,
                i = g.marginTop,
                p = a - (l + j),
                o = b - (i + g.marginBottom),
                n = f(c.chart.valuepadding, 4),
                m = p * 0.7 - n,
                v = o,
                s = h = 0;
                h = 0;
                var u = e.showValue,
                w = e.gaugeWidth,
                r = e.gaugeHeight,
                t = e.gaugeOriginX,
                A = e.gaugeOriginY;
                e.dataLabels = {
                    style: d.plotOptions.series.dataLabels.style
                };
                g = e.dataLabels.style;
                k.setStyle(g);
                h = e.autoScale ? ha(e.origW, e.origH, p, o) : 1;
                e.scaleFactor = h;
                c = ya(d, c, a, b);
                j += c.left;
                o -= pa(d, p, o, this.isHorizontal);
                h = e.tickBottomHeight = 0;
                d = e.data[0];
                if (u && d && d.displayvalue != J) k = k.getSmartText(d.displayvalue, m, v),
                h = n + k.width,
                s = k.height;
                h = Math.max(h, c.right);
                l += h;
                e.valuePadding = n;
                e.valueTextHeight = s;
                e.labelLineHeight = g.lineHeight.split(/px/)[0];
                e.gaugeWidth = w == -1 ? Math.max(a - (j + l), 10) : w;
                e.gaugeHeight = r == -1 ? o: r;
                e.gaugeOriginX = t == -1 ? j + 0 : t;
                e.gaugeOriginY = A == -1 ? e.ticksBelowGauge == 0 ? i + 0 : i: A
            }
        },
        x.bullet);
        x("vbullet", {
            creditLabel: !1,
            defaultSeriesType: "vbullet",
            defaultPlotShadow: 1,
            isHorizontal: !1,
            standaloneInit: !0,
            subTitleFontSizeExtender: 0,
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel,
                e = d.series[0],
                g;
                g = d.chart;
                var h, l = g.marginRight,
                j = g.marginLeft,
                i = g.marginTop,
                p = g.marginBottom,
                o = a - (l + j),
                n = b - (i + p),
                m = f(c.chart.valuepadding, 4),
                v = o * 0.7 - m,
                s = n;
                h = h = 0;
                var u = e.showValue,
                w = e.gaugeWidth,
                r = e.gaugeHeight,
                t = e.gaugeOriginX,
                A = e.gaugeOriginY;
                e.dataLabels = {
                    style: d.plotOptions.series.dataLabels.style
                };
                g = e.dataLabels.style;
                k.setStyle(g);
                h = e.autoScale ? ha(e.origW, e.origH, o, n) : 1;
                e.scaleFactor = h;
                c = fa(d, c, o, n / 2);
                i += c;
                h = pa(d, o, n);
                e.ticksOnRight == 1 ? l += h: j += h;
                h = 0;
                d = e.data[0];
                if (u && d && d.displayvalue != J) k = k.getSmartText(d.displayvalue, v, s),
                h = k.height,
                p += m + h;
                e.valuePadding = m;
                e.valueTextHeight = h;
                e.labelLineHeight = g.lineHeight.split(/px/)[0];
                e.gaugeWidth = w == -1 ? Math.max(a - (j + l), 10) : w;
                e.gaugeHeight = r == -1 ? b - (i + p) : r;
                e.gaugeOriginX = t == -1 ? j: t;
                e.gaugeOriginY = A == -1 ? i: A
            }
        },
        x.bullet);
        x("_realtimearea", {
            creditLabel: !1
        },
        x.linebase);
        x("_realtimecolumn", {
            creditLabel: !1
        },
        x.linebase);
        x("_realtimeline", {
            creditLabel: !1
        },
        x.linebase);
        x("_realtimelinedy", {
            creditLabel: !1
        },
        x.linebase);
        x("_realtimestackedarea", {
            creditLabel: !1
        },
        x.linebase);
        x("_realtimestackedcolumn", {
            creditLabel: !1
        },
        x.linebase);
        x("sparkwinloss", {
            creditLabel: !1
        },
        x.linebase);
        x("thermometer", {
            defaultSeriesType: "thermometer",
            defaultPlotShadow: 1,
            standaloneInit: !0,
            realtimeEnabled: !0,
            linearDataParser: x.angulargauge.linearDataParser,
            defaultGaugePaletteOptions: S(Y, {
                thmBorderColor: ["545454", "60634E", "415D6F", "845001", "68001B"],
                thmFillColor: ["999999", "ADB68F", "A2C4C8", "FDB548", "FF7CA0"]
            }),
            series: function(d, c) {
                var a = {
                    data: [],
                    colorByPoint: !0
                },
                b = d.chart,
                k = c.chart,
                e,
                g = c[K],
                h = this.numberFormatter = g.numberFormatter,
                l = T({},
                d.colorrange),
                j = new da(l.color, void 0, c.chart.paletteIndex),
                i = new X(c.chart.paletteIndex, b.palettethemecolor, this),
                l = h.getCleanValue(d.value),
                h = (e = f(b.showvalue, 1)) ? h.dataLabels(l) : J,
                p = j.getColorObj(l),
                j = p.isOnMeetPoint ? p.nextObj: p.colorObj;
                a.tickValueStyle = c.yAxis[0].labels.style;
                a.showValue = e;
                e = f(this.isPyramid ? b.pyramidyscale: b.funnelyscale);
                a.yScale = e >= 0 && e <= 40 ? e / 200 : 0.2;
                k.backgroundColor = {
                    FCcolor: {
                        color: m(b.bgcolor, i.get2DBgColor()),
                        alpha: m(b.bgalpha, i.get2DBgAlpha()),
                        angle: m(b.bgangle, i.get2DBgAngle()),
                        ratio: m(b.bgratio, i.get2DBgRatio())
                    }
                };
                k.borderColor = G(m(b.bordercolor, i.get2DBorderColor()), m(b.borderalpha, i.get2DBorderAlpha()));
                c.plotOptions.series.dataLabels.style.color = c.title.style.color = c.subtitle.style.color = a.tickValueStyle.color = aa(m(b.basefontcolor, i.get2DBaseFontColor()));
                c.plotOptions.series.dataLabels.style.fontWeight = "bold";
                j || (j = p.nextObj || p.prevObj);
                Z(j.label);
                a.setAdaptiveMin = f(b.setadaptivemin, 0);
                a.upperLimit = z(b.upperlimit);
                a.lowerLimit = z(b.lowerlimit);
                a.upperLimitDisplay = z(b.upperlimitdisplay);
                a.lowerLimitDisplay = z(b.lowerlimitdisplay);
                a.autoScale = f(b.autoscale, 1);
                a.origW = z(b.origw);
                a.origH = z(b.origh);
                a.annRenderDelay = z(b.annrenderdelay);
                a.ticksOnRight = f(b.ticksonright, 1);
                a.showTickMarks = f(b.showtickmarks, 1);
                a.showTickValues = f(b.showtickvalues, a.showTickMarks);
                a.showLimits = f(b.showlimits, a.showTickValues);
                a.adjustTM = f(b.adjusttm, 1);
                a.majorTMNumber = f(b.majortmnumber, -1);
                a.majorTMColor = m(b.majortmcolor, i.getTickColor());
                a.majorTMAlpha = f(b.majortmalpha, 100);
                a.majorTMHeight = f(b.majortmheight, b.majortmwidth, 6);
                a.majorTMThickness = f(b.majortmthickness, 1);
                a.minorTMNumber = f(b.minortmnumber, 4);
                a.minorTMColor = m(b.minortmcolor, a.majorTMColor);
                a.minorTMAlpha = f(b.minortmalpha, a.majorTMAlpha);
                a.minorTMHeight = f(b.minortmheight, b.minortmwidth, Math.round(a.majorTMHeight / 2));
                a.minorTMThickness = f(b.minortmthickness, 1);
                a.tickMarkDistance = f(b.tickmarkdistance, b.tickmarkgap, 1);
                a.tickValueDistance = f(b.tickvaluedistance, b.displayvaluedistance, 2);
                a.tickValueStep = f(b.tickvaluestep, b.tickvaluesstep, 1);
                a.tickValueStep = a.tickValueStep < 1 ? 1 : a.tickValueStep;
                a.useMessageLog = f(b.usemessagelog, 0);
                a.messageLogWPercent = f(b.messagelogwpercent, 80);
                a.messageLogHPercent = f(b.messageloghpercent, 70);
                a.messageLogShowTitle = f(b.messagelogshowtitle, 1);
                a.messageLogTitle = m(b.messagelogtitle, "Message Log");
                a.messageLogColor = m(b.messagelogcolor, i.get2DMsgLogColor());
                a.messageGoesToLog = f(b.messagegoestolog, 1);
                a.messageGoesToJS = f(b.messagegoestojs, 0);
                a.messageJSHandler = m(b.messagejshandler, "alert");
                a.messagePassAllToJS = f(b.messagepassalltojs, 0);
                a.valuePadding = f(b.valuepadding, 4);
                a.dataStreamURL = unescape(m(b.datastreamurl, ""));
                a.streamURLQMarkPresent = a.dataStreamURL.indexOf("?") != -1;
                a.refreshInterval = f(b.refreshinterval, 1);
                a.dataStamp = m(b.datastamp, "");
                a.thmOriginX = z(b.thmoriginx);
                a.thmOriginY = z(b.thmoriginy);
                a.thmBulbRadius = z(b.thmbulbradius);
                a.thmHeight = z(b.thmheight);
                a.gaugeFillMix = z(b.gaugefillmix);
                a.gaugeFillRatio = z(b.gaugefillratio);
                a.showGaugeBorder = f(b.showgaugeborder, 1);
                k = f(b.gaugeborderalpha, 40);
                a.gaugeBorderColor = G(m(b.gaugebordercolor, i.getThmBorderColor()), k);
                a.gaugeBorderThickness = f(b.gaugeborderthickness, 1);
                a.gaugeFillColor = m(b.gaugefillcolor, b.thmfillcolor, i.getThmFillColor());
                a.gaugeFillAlpha = f(b.gaugefillalpha, b.thmfillalpha, V);
                a.thmGlassColor = m(b.thmglasscolor, R(a.gaugeFillColor, 30));
                a.use3DLighting = !f(b.use3dlighting, 1);
                a.shadow = {
                    opacity: f(b.showshadow, 0) ? k / 100 : 0
                };
                g = new ma(b.lowerlimit, b.upperlimit, !1, a, g.numberFormatter);
                g.calculateLimits(l, l);
                g.calculateTicks();
                a.majorTM = g.getMajorTM();
                a.minorTM = g.getMinorTM();
                a.min = g.min;
                a.max = g.max;
                b = f(b.is3d, 1);
                a.is3D = b;
                c.tooltip.enabled = !1;
                a.data[0] = {
                    y: l,
                    displayValue: h,
                    color: G(j.code, j.alpha * 1),
                    colorRange: j,
                    doNotSlice: !0
                };
                c.legend.enabled = !1;
                HCData = a.data;
                c.chart.plotBackgroundColor = O;
                c.chart.plotBorderColor = O;
                a instanceof Array ? c.series = c.series.concat(a) : c.series.push(a);
                this.configureAxis(c, d);
                d.trendlines && ia(d.trendlines, c.yAxis, c[K], !1, this.isBar)
            },
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel,
                e = d.series[0],
                g;
                g = e && e.data[0];
                var h, l = d.chart,
                j, i = c.chart,
                p = a - (l.marginRight + l.marginLeft),
                o = j = b - (l.marginTop + l.marginBottom),
                n = l.marginRight,
                b = l.marginLeft,
                l = l.marginTop,
                i = f(i.valuepadding, 4),
                m = o * 0.7 - i,
                v = a - (n + b);
                h = h = n = a = 0;
                e.dataLabels = {
                    style: d.plotOptions.series.dataLabels.style
                };
                h = e.dataLabels.style;
                k.setStyle(h);
                e.valuePadding = i;
                e.labelLineHeight = h.lineHeight.split(/px/)[0];
                g = k.getSmartText(g.displayValue, v, m);
                k = e.autoScale ? ha(e.origW, e.origH, p, o) : 1;
                e.scaleFactor = k;
                c = fa(d, c, p, j / 2);
                o -= c;
                h = pa(d, p, o);
                e.ticksOnRight == 1 ? n = h: a = h;
                h = 0;
                e.showValue && (h = i + g.height);
                e.valueTextHeight = g.height;
                p = Math.max(p - (a + n), 20);
                d = e.thmHeight = e.thmTotalHeight = f(e.thmHeight, o - h);
                e.thmBulbRadius = e.thmBulbRadius ? e.thmBulbRadius * k: Math.min(p / 2, d * 0.13);
                o = e.thmWidth = e.thmBulbRadius * 1.286;
                p = e.bulbTopHeight = e.thmBulbRadius * 0.766;
                o = e.thmTopHeight = o * 0.5;
                e.thmHeight -= o + p + e.thmBulbRadius;
                e.thmHeight = f(e.thmHeight * k, d - e.thmBulbRadius * 2);
                e.thmOriginX = f(e.thmOriginX * k, b + a) + e.thmBulbRadius;
                e.thmOriginY = f(e.thmOriginY * k, l + c)
            },
            creditLabel: !1
        },
        ja);
        x("vled", {
            defaultSeriesType: "led",
            defaultPlotShadow: 1,
            standaloneInit: !0,
            defaultGaugePaletteOptions: Y,
            realtimeEnabled: !0,
            linearDataParser: x.angulargauge.linearDataParser,
            chartleftmargin: 15,
            chartrightmargin: 15,
            charttopmargin: 10,
            chartbottommargin: 10,
            series: function(d, c) {
                var a = {
                    data: [],
                    colorByPoint: !0
                },
                b = d.chart,
                k,
                e,
                g = c.chart,
                h = c[K],
                l = this.numberFormatter = h.numberFormatter,
                j = T({},
                d.colorrange);
                k = new da(j.color, void 0, g.paletteIndex);
                var i = new X(g.paletteIndex, b.palettethemecolor, this),
                j = l.getCleanValue(d.value) || 0,
                p = f(b.showvalue, b.showrealtimevalue, 1),
                l = p ? l.dataLabels(j) : J,
                o = k.getColorObj(j),
                n = o.isOnMeetPoint ? o.nextObj: o.colorObj,
                C = f(b.placevaluesinside, 0);
                a.tickValueStyle = c.yAxis[0].labels.style;
                e = f(this.isPyramid ? b.pyramidyscale: b.funnelyscale);
                a.yScale = e >= 0 && e <= 40 ? e / 200 : 0.2;
                n || (n = o.nextObj || o.prevObj);
                a.colorRangeManager = k;
                k = Z(n.label);
                g.backgroundColor = {
                    FCcolor: {
                        color: m(b.bgcolor, i.get2DBgColor()),
                        alpha: m(b.bgalpha, i.get2DBgAlpha()),
                        angle: m(b.bgangle, i.get2DBgAngle()),
                        ratio: m(b.bgratio, i.get2DBgRatio())
                    }
                };
                g.borderColor = G(m(b.bordercolor, i.get2DBorderColor()), m(b.borderalpha, i.get2DBorderAlpha()));
                c.plotOptions.series.dataLabels.style.color = c.title.style.color = c.subtitle.style.color = a.tickValueStyle.color = aa(m(b.basefontcolor, i.get2DBaseFontColor()));
                c.plotOptions.series.dataLabels.style.fontWeight = "bold";
                a.setAdaptiveMin = f(b.setadaptivemin, 0);
                a.upperLimit = z(b.upperlimit);
                a.lowerLimit = z(b.lowerlimit);
                a.upperLimitDisplay = z(b.upperlimitdisplay);
                a.lowerLimitDisplay = z(b.lowerlimitdisplay);
                a.autoScale = f(b.autoscale, 1);
                a.origW = z(b.origw);
                a.origH = z(b.origh);
                a.annRenderDelay = z(b.annrenderdelay);
                a.ticksOnRight = f(b.ticksonright, 1);
                a.ticksOnRight = f(b.ticksonright, 1);
                a.ticksBelowGauge = f(b.ticksbelowgauge, 1);
                a.showTickMarks = f(b.showtickmarks, 1);
                a.showTickValues = f(b.showtickvalues, a.showTickMarks);
                a.showLimits = f(b.showlimits, a.showTickValues);
                a.adjustTM = f(b.adjusttm, 1);
                a.majorTMNumber = f(b.majortmnumber, -1);
                a.majorTMColor = m(b.majortmcolor, i.getTickColor());
                a.majorTMAlpha = f(b.majortmalpha, 100);
                a.majorTMHeight = f(b.majortmheight, b.majortmwidth, 6);
                a.majorTMThickness = f(b.majortmthickness, 1);
                a.minorTMNumber = f(b.minortmnumber, 4);
                a.minorTMColor = m(b.minortmcolor, a.majorTMColor);
                a.minorTMAlpha = f(b.minortmalpha, a.majorTMAlpha);
                a.minorTMHeight = f(b.minortmheight, b.minortmwidth, Math.round(a.majorTMHeight / 2));
                a.minorTMThickness = f(b.minortmthickness, 1);
                a.tickMarkDistance = f(b.tickmarkdistance, b.tickmarkgap, 3);
                a.tickValueDistance = f(b.tickvaluedistance, b.displayvaluedistance, 3);
                a.tickValueStep = f(b.tickvaluestep, b.tickvaluesstep, 1);
                a.tickValueStep = a.tickValueStep < 1 ? 1 : a.tickValueStep;
                a.connectTickMarks = f(b.connecttickmarks, this.isHorizontal ? 1 : 0);
                a.useMessageLog = f(b.usemessagelog, 0);
                a.messageLogWPercent = f(b.messagelogwpercent, 80);
                a.messageLogHPercent = f(b.messageloghpercent, 70);
                a.messageLogShowTitle = f(b.messagelogshowtitle, 1);
                a.messageLogTitle = m(b.messagelogtitle, "Message Log");
                a.messageLogColor = m(b.messagelogcolor, i.get2DMsgLogColor());
                a.messageGoesToLog = f(b.messagegoestolog, 1);
                a.messageGoesToJS = f(b.messagegoestojs, 0);
                a.messageJSHandler = m(b.messagejshandler, "alert");
                a.messagePassAllToJS = f(b.messagepassalltojs, 0);
                a.showValue = p;
                a.valuePadding = f(b.valuepadding, 4);
                a.dataStreamURL = unescape(m(b.datastreamurl, ""));
                a.streamURLQMarkPresent = a.dataStreamURL.indexOf("?") != -1;
                a.refreshInterval = f(b.refreshinterval, 1);
                a.dataStamp = m(b.datastamp, "");
                a.showGaugeBorder = f(b.showgaugeborder, 1);
                a.gaugeBorderColor = m(b.gaugebordercolor, "333333");
                a.gaugeBorderThickness = f(b.gaugeborderthickness, a.showGaugeBorder ? 2 : 0);
                a.gaugeBorderAlpha = m(b.gaugeborderalpha, V);
                a.gaugeOriginX = f(b.gaugeoriginx, b.ledoriginx, -1);
                a.gaugeOriginY = f(b.gaugeoriginy, b.ledoriginy, -1);
                a.gaugeHeight = f(b.gaugeheight, b.ledheight, -1);
                a.gaugeWidth = f(b.gaugewidth, b.ledwidth, -1);
                a.gaugeFillColor = m(b.gaugefillcolor, b.ledbgcolor, "000000");
                a.ledGap = f(b.ledgap, 2);
                a.ledSize = f(b.ledsize, 2);
                a.useSameFillColor = f(b.usesamefillcolor, 0);
                a.useSameFillBgColor = f(b.usesamefillbgcolor, a.useSameFillColor);
                a.reverseScale = f(b.reversescale, 0) == 1;
                a.shadow = {
                    opacity: f(b.showshadow, 1) ? a.gaugeBorderAlpha / 100 : 0
                };
                g = new ma(b.lowerlimit, b.upperlimit, !1, a, h.numberFormatter);
                g.calculateLimits(j, j);
                g.calculateTicks();
                a.majorTM = g.getMajorTM();
                a.minorTM = g.getMinorTM();
                a.min = g.min;
                a.max = g.max;
                g = f(b.is3d, 1);
                b = f(b.usecolornameasvalue, 0);
                a.is3D = g;
                a.placeValuesInside = C;
                c.tooltip.enabled = !1;
                a.data[0] = {
                    y: j,
                    displayValue: b ? k: l,
                    color: G(n.code, n.alpha * 1),
                    colorRange: n,
                    doNotSlice: !0
                };
                c.legend.enabled = !1;
                HCData = a.data;
                c.chart.plotBackgroundColor = O;
                c.chart.plotBorderColor = O;
                a instanceof Array ? c.series = c.series.concat(a) : c.series.push(a);
                this.configureAxis(c, d);
                d.trendlines && ia(d.trendlines, c.yAxis, c[K], !1, this.isBar)
            },
            spaceManager: function(d, c, a, b) {
                var k = d[K].smartLabel,
                e = d.series[0],
                g = e && e.data[0],
                h = d.chart,
                l,
                j = c.chart,
                i = a - (h.marginRight + h.marginLeft),
                b = l = b - (h.marginTop + h.marginBottom),
                p = h.marginRight,
                o = h.marginLeft,
                h = h.marginTop,
                j = f(j.valuepadding, 4),
                n = b * 0.7 - j,
                m = a - (p + o),
                v = p = 0,
                s = 0,
                s = 0,
                u = e.gaugeWidth,
                w = e.gaugeHeight,
                r = e.gaugeOriginX,
                t = e.gaugeOriginY,
                A = e.ticksBelowGauge == 0;
                e.dataLabels = {
                    style: d.plotOptions.series.dataLabels.style
                };
                a = e.dataLabels.style;
                k.setStyle(a);
                k = k.getSmartText(g.displayValue, m, n);
                g = e.autoScale ? ha(e.origW, e.origH, i, b) : 1;
                e.scaleFactor = g;
                c = fa(d, c, i, l / 2);
                b -= c;
                s = pa(d, i, b, this.isHorizontal);
                this.isHorizontal ? (b -= s, h += A ? s: 0, e.tickBottomHeight = A ? 0 : s) : (e.ticksOnRight == 1 ? v = s: p = s, e.tickBottomHeight = 0);
                s = 0;
                e.showValue && (s = j + k.height);
                e.valuePadding = j;
                e.valueTextHeight = k.height;
                e.labelLineHeight = a.lineHeight.split(/px/)[0];
                e.gaugeWidth = u == -1 ? Math.max(i - (p + v), 10) : u;
                e.gaugeHeight = w == -1 ? f(b - s) : w;
                e.gaugeOriginX = r == -1 ? o + p: r;
                e.gaugeOriginY = t == -1 ? A ? h + c + 0 : h + c: t
            },
            creditLabel: !1
        },
        ja);
        x("hled", {
            defaultPlotShadow: 1,
            standaloneInit: !0,
            defaultGaugePaletteOptions: Y,
            creditLabel: !1,
            isHorizontal: !0
        },
        x.vled);
        var Oa = function(d, c) {
            return d.minvalue - c.minvalue
        };
        da.prototype = {
            getColorObj: function(d) {
                for (var c = this.colorArr,
                a = 0,
                b = c.length,
                k, e, g = {}; a < b; a += 1) {
                    g.index = a;
                    k = c[a];
                    e = c[a + 1];
                    if (d < k.minvalue) return g.nextObj = k,
                    g;
                    if (d >= k.minvalue && d <= k.maxvalue) {
                        g.colorObj = k;
                        if (e && d == e.minvalue) g.nextObj = e,
                        g.isOnMeetPoint = !0;
                        return g
                    }
                    g.prevObj = k
                }
                g.index = a - 1;
                return g
            },
            getColorRangeArr: function(d, c) {
                var a, b = this.colorArr,
                k, e = [];
                if (!this.defaultAsigned && (d > c && (a = d, d = c, c = a), d < c && (a = this.getColorObj(d), k = this.getColorObj(c), a && k))) {
                    a = a.index;
                    k = k.index;
                    e.push(T(T({},
                    b[a]), {
                        minvalue: d
                    }));
                    for (a += 1; a < k; a += 1) e.push(b[a]);
                    e.push(T(T({},
                    b[k]), {
                        maxvalue: c
                    }))
                }
                return e
            }
        };
        da.prototype.constructor = da;
        var Ya = function() {
            var d = {
                y: !0,
                R1: !0,
                R2: !0,
                h: !0,
                r3dFactor: !0,
                color: !0,
                alpha: !0,
                fill: !0,
                stroke: !0,
                strokeColor: !0,
                strokeAlpha: !0,
                "stroke-width": !0
            },
            c = function(b, a, c, d, l, j, i, f, o, n) {
                if (W(b)) a = b.y,
                c = b.R1,
                d = b.R2,
                l = b.h,
                j = b.r3dFactor,
                i = b.is2D,
                n = b.isHollow,
                o = b.isFunnel,
                f = b.renderer,
                b = b.x;
                var m = b - c,
                v = b + c,
                s = b - d,
                u = b + d,
                w = a + l,
                r;
                if (i) {
                    if (r = {
                        silhuette: ["M", m, a, "L", v, a, u, w, s, w, "Z"]
                    },
                    !o) r.lighterHalf = ["M", m, a, "L", b, a, b, w, s, w, "Z"],
                    r.darkerHalf = ["M", b, a, "L", v, a, u, w, b, w, "Z"]
                } else if (o) {
                    m = c;
                    s = d;
                    v = n;
                    u = f;
                    l = a + l;
                    w = m * j;
                    j *= s;
                    var c = Math.pow(s, 2) - Math.pow(m, 2),
                    d = -2 * (Math.pow(s, 2) * a - Math.pow(m, 2) * l),
                    n = Math.sqrt(Math.pow(d, 2) - 4 * c * (Math.pow(m * j, 2) + Math.pow(s * a, 2) - Math.pow(s * w, 2) - Math.pow(m * l, 2))),
                    f = ( - d + n) / (2 * c),
                    c = ( - d - n) / (2 * c),
                    t;
                    f < l && f > a ? t = c: c < l && c > a && (t = f);
                    f = Math.sqrt((Math.pow(t - a, 2) - Math.pow(w, 2)) / Math.pow(m, 2));
                    c = -f;
                    c = {
                        topLeft: {
                            x: Math.round(Math.pow(m, 2) * c / (t - a) * 100) / 100,
                            y: Math.round((Math.pow(w, 2) / (t - a) + a) * 100) / 100
                        },
                        bottomLeft: {
                            x: Math.round(Math.pow(s, 2) * c / (t - l) * 100) / 100,
                            y: Math.round((Math.pow(j, 2) / (t - l) + l) * 100) / 100
                        },
                        topRight: {
                            x: Math.round(Math.pow(m, 2) * f / (t - a) * 100) / 100,
                            y: Math.round((Math.pow(w, 2) / (t - a) + a) * 100) / 100
                        },
                        bottomRight: {
                            x: Math.round(Math.pow(s, 2) * f / (t - l) * 100) / 100,
                            y: Math.round((Math.pow(j, 2) / (t - l) + l) * 100) / 100
                        }
                    };
                    for (r in c) if (isNaN(c[r].x) || isNaN(c[r].y)) c[r].x = r == "topLeft" || r == "bottomLeft" ? -m: m,
                    c[r].y = a;
                    d = c.topLeft;
                    n = c.bottomLeft;
                    r = b + d.x;
                    t = b + c.topRight.x;
                    var f = b + n.x,
                    c = b + c.bottomRight.x,
                    d = d.y,
                    o = n.y,
                    n = u.getArcPath(b, a, r, d, t, d, m, w, 0, 0),
                    i = u.getArcPath(b, a, r, d, t, d, m, w, 1, 1),
                    A = u.getArcPath(b, l, c, o, f, o, s, j, 1, 0),
                    s = u.getArcPath(b, l, c, o, f, o, s, j, 0, 1),
                    s = {
                        front: ["M", r, d].concat(n, ["L", c, o], A, ["Z"]),
                        back: ["M", r, d].concat(i, ["L", c, o], s, ["Z"]),
                        silhuette: ["M", r, d].concat(i, ["L", c, o], A, ["Z"])
                    };
                    if (!v) s.top = ["M", r, d].concat(n, ["L", t, d], u.getArcPath(b, a, t, d, r, d, m, w, 0, 1), ["Z"]);
                    r = s
                } else r = c * j,
                t = d * j,
                l = Ea(5, c),
                f = Ea(2, 2 * r),
                c = Ea(2, f),
                j = c / j,
                r = {
                    top: ["M", m, a, "L", b, a + r, v, a, b, a - r, "Z"],
                    front: ["M", m, a, "L", b, a + r, v, a, u, w, b, w + t, s, w, "Z"],
                    topLight: ["M", m, a + 0.5, "L", b, a + r + 0.5, b, a + r - f, m + j, a, "Z"],
                    topLight1: ["M", v, a + 0.5, "L", b, a + r + 0.5, b, a + r - c, v - j, a, "Z"],
                    silhuette: ["M", m, a, "L", b, a - r, v, a, u, w, b, w + t, s, w, "Z"],
                    centerLight: ["M", b, a + r, "L", b, w + t, b - 5, w + t, b - l, a + r, "Z"],
                    centerLight1: ["M", b, a + r, "L", b, w + t, b + 5, w + t, b + l, a + r, "Z"]
                };
                return r
            },
            a = function(b, a) {
                var g, h, l = this,
                j, i, p = !1,
                o = !1,
                n = this._3dAttr;
                ea(b) && U(a) && (g = b, b = {},
                b[g] = a);
                if (ea(b)) l = d[b] ? this._3dAttr[b] : this._attr(b);
                else {
                    for (g in b) if (h = b[g], d[g]) if (n[g] = h, g === "fill") h && h.linearGradient && h.stops && h.stops[0] && (h = h.stops[0][1]),
                    wa.test(h) ? (i = ua(h), j = i.get("hex"), i = i.get("a") * 100) : h && h.FCcolor ? (j = h.FCcolor.color.split(q)[0], i = h.FCcolor.alpha.split(q)[0]) : va.test(h) && (j = h.replace(ra, sa), i = f(n.alpha, 100)),
                    n.color = j,
                    n.alpha = i,
                    o = !0;
                    else if (g === "color" || g === "alpha") n.fill = G(n.color, f(n.alpha, 100)),
                    o = !0;
                    else if (g === "stroke" || g === "strokeColor" || g === "strokeAlpha") {
                        if (n.is2D) g === "stroke" ? (h && h.linearGradient && h.stops && h.stops[0] && (h = h.stops[0][1]), wa.test(h) ? (i = ua(h), j = i.get("hex"), i = i.get("a") * 100) : h && h.FCcolor ? (j = h.FCcolor.color.split(q)[0], i = h.FCcolor.alpha.split(q)[0]) : va.test(h) && (j = h.replace(ra, sa), i = f(n.alpha, 100)), n.strokeColor = j, n.strokeAlpha = i) : n.stroke = G(n.strokeColor, f(n.strokeAlpha, 100)),
                        n.isFunnel ? this.funnel2D.attr("stroke", n.stroke) : this.borderElement.attr("stroke", n.stroke)
                    } else g === "stroke-width" ? n.is2D && (n.isFunnel ? this.funnel2D.attr(g, h) : this.borderElement.attr(g, h)) : p = !0;
                    else this._attr(g, h);
                    if (n.is2D) p && (Shapeargs = c(n.x, n.y, n.R1, n.R2, n.h, n.r3dFactor, n.is2D), l.shadowElement.attr({
                        d: Shapeargs.silhuette
                    }), n.isFunnel ? l.funnel2D.attr({
                        d: Shapeargs.silhuette
                    }) : (l.lighterHalf.attr({
                        d: Shapeargs.lighterHalf
                    }), l.darkerHalf.attr({
                        d: Shapeargs.darkerHalf
                    }), l.borderElement.attr({
                        d: Shapeargs.silhuette
                    }))),
                    o && (n.isFunnel ? l.funnel2D.attr("fill", G(n.color, f(n.alpha, 100))) : (j = L(n.color, 80), i = R(n.color, 80), l.lighterHalf.attr("fill", G(i, f(n.alpha, 100))), l.darkerHalf.attr("fill", G(j, f(n.alpha, 100)))));
                    else if (p && (Shapeargs = c(n.x, n.y, n.R1, n.R2, n.h, n.r3dFactor, n.is2D), l.shadowElement.attr("d", Shapeargs.silhuette), n.isFunnel ? (l.front.attr("d", Shapeargs.front), l.back.attr("d", Shapeargs.back), l.top && Shapeargs.top && l.top.attr("d", Shapeargs.top)) : (l.front.attr("d", Shapeargs.front), l.top.attr("d", Shapeargs.top), l.topLight.attr("d", Shapeargs.topLight), l.topLight1.attr("d", Shapeargs.topLight1), l.centerLight.attr("d", Shapeargs.centerLight), l.centerLight1.attr("d", Shapeargs.centerLight1))), o) if (j = n.color, i = n.alpha, n.isFunnel) g = R(j, 60),
                    o = L(j, 60),
                    l.back.attr("fill", {
                        FCcolor: {
                            color: o + q + g + q + j,
                            alpha: i + q + i + q + i,
                            ratio: "0,60,40",
                            angle: 0
                        }
                    }),
                    l.front.attr("fill", {
                        FCcolor: {
                            color: j + q + g + q + o,
                            alpha: i + q + i + q + i,
                            ratio: "0,40,60",
                            angle: 0
                        }
                    }),
                    l.top && l.top.attr("fill", {
                        FCcolor: {
                            color: g + q + o,
                            alpha: i + q + i,
                            ratio: "0,100",
                            angle: -65
                        }
                    });
                    else {
                        g = R(j, 80);
                        p = R(j, 70);
                        o = L(j, 80);
                        h = "0," + i;
                        var m = j + q + p,
                        n = 5 / (n.R1 * n.r3dFactor) * 100;
                        l.centerLight.attr("fill", {
                            FCcolor: {
                                color: m,
                                alpha: h,
                                ratio: "0,100",
                                angle: 0
                            }
                        });
                        l.centerLight1.attr("fill", {
                            FCcolor: {
                                color: m,
                                alpha: h,
                                ratio: "0,100",
                                angle: 180
                            }
                        });
                        l.topLight.attr("fill", {
                            FCcolor: {
                                color: p + q + p + q + j + q + j,
                                alpha: i + q + i + q + 0 + q + 0,
                                ratio: "0,50," + n + q + (50 - n),
                                angle: -45
                            }
                        });
                        l.topLight1.attr("fill", {
                            FCcolor: {
                                color: p + q + j + q + o,
                                alpha: i + q + i + q + i,
                                ratio: "0,50,50",
                                angle: 0
                            }
                        });
                        l.front.attr("fill", {
                            FCcolor: {
                                color: j + q + j + q + o + q + o,
                                alpha: i + q + i + q + i + q + i,
                                ratio: "0,50,0,50",
                                angle: 0
                            }
                        });
                        l.top.attr("fill", {
                            FCcolor: {
                                color: g + q + j + q + o + q + o,
                                alpha: i + q + i + q + i + q + i,
                                ratio: "0,25,30,45",
                                angle: -45
                            }
                        })
                    }
                }
                return l
            },
            b = function() {
                var a = this.shadowElement;
                b && a.shadow.apply(a, arguments)
            };
            return function(d, e, g, h, l, j, i, p, o, n, m) {
                if (W(d)) e = d.y,
                g = d.R1,
                h = d.R2,
                l = d.h,
                j = d.r3dFactor,
                i = d.gStr,
                p = d.is2D,
                o = d.renderer,
                m = d.isHollow,
                n = d.isFunnel,
                d = d.x;
                j = f(j, 0.15);
                d = {
                    x: d,
                    y: e,
                    R1: g,
                    R2: h,
                    h: l,
                    r3dFactor: j,
                    is2D: p,
                    isHollow: m,
                    isFunnel: n,
                    renderer: o
                };
                e = c(d);
                i = o.g(i);
                i.shadowElement = o.path(e.silhuette).attr({
                    fill: Ja
                }).add(i);
                i._attr = i.attr;
                i.attr = a;
                i.shadow = b;
                i._3dAttr = d;
                if (n) if (p) i.funnel2D = o.path(e.silhuette).add(i);
                else {
                    if (i.back = o.path(e.back).attr({
                        "stroke-width": 0
                    }).add(i), i.front = o.path(e.front).attr({
                        "stroke-width": 0
                    }).add(i), e.top) i.top = o.path(e.top).attr({
                        "stroke-width": 0
                    }).add(i)
                } else p ? (i.lighterHalf = o.path(e.lighterHalf).attr({
                    "stroke-width": 0
                }).add(i), i.darkerHalf = o.path(e.darkerHalf).attr({
                    "stroke-width": 0
                }).add(i), i.borderElement = o.path(e.silhuette).attr({
                    fill: Ja
                }).add(i)) : (i.front = o.path(e.front).attr({
                    "stroke-width": 0
                }).add(i), i.centerLight = o.path(e.centerLight).attr({
                    "stroke-width": 0
                }).add(i), i.centerLight1 = o.path(e.centerLight1).attr({
                    "stroke-width": 0
                }).add(i), i.top = o.path(e.top).attr({
                    "stroke-width": 0
                }).add(i), i.topLight = o.path(e.topLight).attr({
                    "stroke-width": 0
                }).add(i), i.topLight1 = o.path(e.topLight1).attr({
                    "stroke-width": 0
                }).add(i));
                return i
            }
        } ();
        y.funnel = S(y.pie, {
            states: {
                hover: {}
            }
        });
        x = Q.extendClass(N.pie, {
            type: "funnel",
            translate: function() {
                var d = this.data,
                c = this.chart,
                a = this.options,
                b = c.plotWidth / 2,
                k = this.type === "funnel",
                e = c.plotHeight,
                g = d[d.length - 1].y,
                h = d[0].y,
                f,
                j,
                i,
                p,
                o = 0,
                n = a.yScale,
                m = a.isHollow,
                v = a.is2d,
                s = 0,
                u = c.renderer,
                w = a.streamlinedData,
                r = a.connectorWidth,
                t = 0.8 / e;
                f = w ? e / (h - g) : e / h;
                j = b;
                oa(d,
                function(c, e) {
                    e ? (w ? (i = a.useSameSlantAngle == 1 ? b * c.y / h: b * Math.sqrt(c.y / h), p = f * (d[e - 1].y - c.y)) : (s += p = f * d[e].y, i = b * (1 - s * t)), c.shapeArgs = {
                        x: b,
                        y: o,
                        R1: j,
                        R2: i,
                        h: p,
                        r3dFactor: n,
                        isHollow: m,
                        gStr: "point",
                        is2D: v,
                        renderer: u,
                        isFunnel: !0
                    },
                    j = i, o += p, c.LabelAline = "left", c.labelX = r + (j + i) / 2) : (i = a.useSameSlantAngle == 1 ? b * d[0].y / h: b * Math.sqrt(d[0].y / h), c.labelWidht > i * 2 ? (c.LabelAline = "left", c.labelX = -b) : c.LabelAline = "center");
                    c.plotX = b;
                    c.plotY = o;
                    if (!e && k) c.labelY = (v ? c.plotY: -n * j) - parseInt(a.dataLabels.style.lineHeight)
                })
            },
            drawPoints: function() {
                for (var d = this.data,
                c = this.chart,
                a = d.length - 1,
                b; a >= 0; a -= 1) if (b = d[a], b.shapeArgs) {
                    if (!b.graphic) b.graphic = Ya(b.shapeArgs);
                    b.graphic.attr({
                        color: b.color,
                        alpha: b.alpha,
                        "stroke-width": b.borderWidth,
                        stroke: b.borderColor
                    }).translate(c.plotLeft, c.plotTop).add()
                }
            },
            drawDataLabels: function() {
                var d = this.data,
                c = this.chart,
                a = this.options.dataLabels,
                b = this.dataLabelsGroup,
                k, e, g = c.renderer,
                h = this.options.showLabelsAtCenter,
                f = {
                    "stroke-width": a.connectorWidth,
                    stroke: a.connectorColor
                },
                j = Number(a.style.lineHeight.split(/px/)[0]),
                i = j * 0.3,
                p,
                o,
                n,
                m;
                if (!b) b = this.dataLabelsGroup = g.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN,
                    zIndex: 6
                }).translate(c.plotLeft, c.plotTop).add(),
                c.options.chart.hasScroll && b.clip(this.clipRect);
                for (n = d.length - 1; n >= 0; n -= 1) m = d[n],
                k = m.plotY,
                c = m.plotX,
                m.labelY && (k += m.labelY),
                h ? e = "center": (m.labelX && (c += m.labelX), e = k, p !== void 0 && o !== void 0 && o - k < j && (k = o - j), p = m.plotY, o = k, m.displayValue !== J && !(n === 0 && this.type == "funnel") && (e = ["M", c - this.options.labelDistance, e, "L", c, k], g.path(e).attr(f).add(b)), k += i, e = m.LabelAline),
                g.text(m.displayValue, c + 3, k).attr({
                    align: e
                }).css(a.style).add(b)
            },
            drawTracker: function() {
                var d = this,
                c = d.chart,
                a, b = +new Date,
                k;
                c.options.chart.hasScroll && c.trackerGroup.clip(d.clipRect);
                oa(d.data,
                function(e) {
                    a = e.trackerEventAdded;
                    if (e.y !== null && !a && e.graphic) {
                        if (e.link !== void 0) var g = {
                            cursor: "pointer",
                            _cursor: "hand"
                        };
                        e.graphic.attr({
                            isTracker: b
                        }).on(Ia ? "touchstart": "mouseover",
                        function(a) {
                            k = a.relatedTarget || a.fromElement;
                            if (c.hoverSeries !== d && ta(k, "isTracker") !== b) d.onMouseOver();
                            e.onMouseOver()
                        }).on("mouseout",
                        function(a) {
                            if (!d.options.stickyTracking && (k = a.relatedTarget || a.toElement, ta(k, "isTracker") !== b)) d.onMouseOut()
                        }).css(g);
                        e.trackerEventAdded = !0
                    }
                })
            },
            animate: function() {
                var d, c, a, b = this.data.length,
                k = this.options.animation;
                k && !W(k) && (k = {});
                for (d = 0; d < b; d += 1) if ((a = (c = this.data[d]) && c.graphic) && !a.isAnimating) a.isAnimating = !0,
                a.attr({
                    alpha: 0,
                    strokeAlpha: 0
                }),
                a.animate({
                    alpha: c.alpha,
                    strokeAlpha: ua(c.borderColor).get("a") * 100
                },
                k);
                this.animate = null
            }
        });
        N.funnel = x;
        y.pyramid = S(y.funnel, {
            states: {
                hover: {}
            }
        });
        x = Q.extendClass(N.funnel, {
            type: "pyramid",
            translate: function() {
                var d = this.data,
                c = this.chart,
                a = this.options,
                b = c.plotWidth / 2,
                k, e, g, h, f = c.renderer,
                j = a.valueSum,
                i = a.is2d,
                p = a.connectorWidth,
                o = 0,
                n = 0,
                m = a.yScale;
                k = c.plotHeight / j;
                e = 0;
                oa(d,
                function(a) {
                    o += a.y;
                    g = b * o / j;
                    h = k * a.y;
                    a.shapeArgs = {
                        x: b,
                        y: n,
                        R1: e,
                        R2: g,
                        h: h,
                        r3dFactor: m,
                        gStr: "point",
                        is2D: i,
                        renderer: f
                    };
                    n += h;
                    a.LabelAline = "left";
                    a.labelX = p + (e + g) / 2;
                    a.plotX = b;
                    a.plotY = n - h / 2;
                    e = g
                })
            }
        });
        N.pyramid = x;
        y.bulb = S(y.pie, {
            states: {
                hover: {}
            }
        });
        x = Q.extendClass(N.pie, {
            type: "bulb",
            drawPoints: function() {
                var d, c = this.chart;
                d = this.options;
                var a, b, k = d.gaugeRadius,
                e = d.gaugeOriginX,
                g = d.gaugeOriginY;
                a = this.data[0];
                b = a.graphic;
                if (a.y !== void 0 && !isNaN(a.y)) d = {
                    r: k,
                    fill: a.color,
                    stroke: a.borderColor,
                    "stroke-linecap": "round",
                    "stroke-width": a.borderWidth
                },
                a.plotX = e,
                a.plotY = g,
                a.radius = k,
                b ? b.animate({
                    x: e,
                    y: g,
                    r: k
                }) : a.graphic = c.renderer.symbol("circle", e, g, k).attr(d).add(this.chart.seriesGroup)
            },
            drawDataLabels: function() {
                var d = this.chart,
                c = this.options,
                a = this.data[0],
                b = this.dataLabelsGroup,
                k;
                if (!b) b = this.dataLabelsGroup = d.renderer.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN,
                    zIndex: 6
                }).add();
                k = c.placeValuesInside ? a.plotY + c.labelLineHeight * 1 - c.valueTextHeight / 2 : a.plotY + a.radius + c.valuePadding + c.labelLineHeight * 1;
                if (a.y !== void 0 && !isNaN(a.y)) a.dataLabel = d.renderer.text(a.displayValue, a.plotX, k).attr({
                    align: "center"
                }).css(c.dataLabels.style).add(b)
            },
            animate: function() {
                var d, c, a = this.options.animation;
                a && !W(a) && (a = {});
                if ((c = (d = this.data[0]) && d.graphic) && !c.isAnimating) c.isAnimating = !0,
                c.attr({
                    r: 0
                }),
                c.animate({
                    r: d.radius
                },
                a);
                this.animate = null
            },
            realtimeUpdate: function(d) {
                var c = this && this.options,
                a = this && this.chart,
                b = this && this.data && this.data[0],
                k = {},
                e,
                g,
                h = a.options.instanceAPI.numberFormatter;
                e = a.options.instanceAPI.getPointColor;
                var f, j = d.values || [],
                i = d.labels || [],
                p = d.toolTexts || [],
                d = d.showLabels || [];
                if (b && (j[0] && (g = c.colorRangeGetter.getColorObj(h.getCleanValue(j[0])), (k = g.isOnMeetPoint ? g.nextObj: g.colorObj) || (k = g.nextObj || g.prevObj), g = c.defaultColors[g.index % c.defaultColLen], e = c.is3D ? e(m(k.code, g), c.gaugeFillAlpha) : G(m(k.code, g), c.gaugeFillAlpha), b.graphic.attr({
                    fill: e
                })), d[0] == 0 ? f = "": j[0] !== void 0 && (f = c.useColorNameAsValue ? m(k.name, k.label, i[0]) : h.dataLabels(j[0])), f !== void 0 ? (b.dataLabel.attr({
                    text: f
                }), c = m(p[0], f)) : c = p[0], c !== void 0)) b.toolText = c,
                a.tooltip && a.tooltip.refresh(b, !0)
            },
            render: function() {
                var b;
                var d, c = this.chart;
                d = c.renderer;
                var a = this.options;
                if (!this.group) b = this.group = d.g("series"),
                d = b,
                d.attr({
                    visibility: this.visible ? ba: HIDDEN,
                    zIndex: a.zIndex
                }).translate(c.plotLeft, c.plotTop).add(c.seriesGroup);
                this.drawPoints();
                this.drawTracker();
                this.drawDataLabels();
                if (this.visible) c.currentSeriesIndex = this.index,
                c.naviigator && placeNaviGator(c);
                this.options.animation && this.animate && this.animate();
                this.isDirty = !1
            }
        });
        N.bulb = x;
        y.cylinder = S(y.bulb, {
            states: {
                hover: {}
            }
        });
        var Za = function() {
            var d = {
                fulidHRatio: !0,
                color: !0,
                alpha: !0,
                fill: !0
            },
            c = [],
            a = function(b, a) {
                var g, h, l = this,
                j, i, p = !1,
                o = !1,
                n = this._3dAttr;
                ea(b) && U(a) && (g = b, b = {},
                b[g] = a);
                if (ea(b)) l = d[b] ? l._3dAttr[b] : l._attr(b);
                else for (g in b) if (h = b[g], d[g]) {
                    if (g === "fill") h && h.linearGradient && h.stops && h.stops[0] && (h = h.stops[0][1]),
                    wa.test(h) ? (i = ua(h), j = i.get("hex"), i = i.get("a") * 100) : h && h.FCcolor ? (j = h.FCcolor.color.split(q)[0], i = h.FCcolor.alpha.split(q)[0]) : va.test(h) && (j = h.replace(ra, sa)),
                    n.fluidColor = m(j, n.fluidColor, "000000"),
                    n.fluidAlpha = f(i, n.fluidAlpha, 100),
                    p = !0;
                    else if (g === "color") n.fluidColor = m(h, n.fluidColor, "000000"),
                    p = !0;
                    else if (g === "alpha") n.fluidAlpha = f(h, n.fluidAlpha, 100),
                    p = !0;
                    else if (h >= 0 && h <= 1) n.fulidHRatio = h,
                    o = !0;
                    if (p) {
                        h = L(n.fluidColor, 70);
                        var C = R(n.fluidColor, 70),
                        v = L(n.conColor, 80),
                        s = R(n.conColor, 80),
                        u;
                        i = n.fluidAlpha;
                        u = i + q + i;
                        l.fluid.attr({
                            "stroke-width": 0,
                            fill: {
                                FCcolor: {
                                    gradientUnits: "objectBoundingBox",
                                    cx: 0.5,
                                    cy: 0,
                                    r: "100%",
                                    color: C + q + h,
                                    alpha: u,
                                    ratio: "0,100",
                                    radialGradient: !0
                                }
                            }
                        });
                        l.fluidTop.attr({
                            "stroke-width": 3,
                            stroke: G(C, i),
                            fill: {
                                FCcolor: {
                                    gradientUnits: "objectBoundingBox",
                                    cx: 0.5,
                                    cy: 0.7,
                                    r: "100%",
                                    color: C + q + h,
                                    alpha: u,
                                    ratio: "0,100",
                                    radialGradient: !0
                                }
                            }
                        });
                        l.btnBorderLight.attr({
                            fill: {
                                FCcolor: {
                                    color: s + q + v + q + s + q + s + q + v + q + h + q + v + q + s,
                                    alpha: "50,50,50,50,50," + i * 0.7 + ",50,50",
                                    ratio: "0,15,0,12,0,15,43,15",
                                    angle: 0
                                }
                            }
                        })
                    }
                    if (o) {
                        h = n.x;
                        var C = n.r,
                        w = n.fluidStroke / 2,
                        v = h - C,
                        s = h + C;
                        u = v + w;
                        var r = s - w,
                        t = n.y + n.h,
                        A = t - n.h * n.fulidHRatio,
                        P = C * n.r3dFactor,
                        w = C - w,
                        D = n.renderer.getArcPath;
                        l.fluid.attr({
                            d: c.concat(["M", v, t], D(h, t, v, t, s, t, C, P, 0, 0), ["L", s, A], D(h, A, s, A, v, A, C, P, 1, 0), ["Z"])
                        });
                        l.fluidTop.attr({
                            d: c.concat(["M", u, A], D(h, A, u, A, r, A, w, P, 0, 0), ["L", r, A], D(h, A, r, A, u, A, w, P, 0, 0), ["Z"])
                        })
                    }
                } else this._attr(g, h);
                return l
            },
            b = function() {};
            return function(d, e, g, h, l, j, i, p, o, n, C, v) {
                if (W(d)) e = d.y,
                g = d.r,
                h = d.h,
                l = d.r3dFactor,
                j = d.gStr,
                i = d.renderer,
                p = d.fulidHRatio,
                o = d.conColor,
                n = d.conAlpha,
                C = d.fluidColor,
                v = d.fluidAlpha,
                d = d.x;
                l = f(l, 0.15);
                p >= 0 && p <= 1 || (p = 0);
                o = m(o, "FFFFFF");
                n = f(n, 30);
                C = m(C, "000000");
                v = f(v, 100);
                v = {
                    x: d,
                    y: e,
                    r: g,
                    h: h,
                    r3dFactor: l,
                    renderer: i,
                    fulidHRatio: p,
                    conColor: o,
                    conAlpha: n,
                    fluidStroke: 3,
                    fluidColor: C,
                    fluidAlpha: v
                };
                n = i.getArcPath;
                j = i.g(j);
                j._attr = j.attr;
                j.attr = a;
                j.shadow = b;
                j._3dAttr = v;
                l *= g;
                var v = g - 1.5,
                s = e + h,
                h = s - h * p,
                p = d - g,
                u = d + g,
                w = p + 1.5,
                r = u - 1.5,
                t = p - 2,
                A = u + 2,
                P = g + 2,
                D = l + 2,
                I = s + 4,
                F = I + 0.0010,
                E = I + 1,
                B = L(o, 80),
                x = L(o, 90),
                o = R(o, 80),
                H = L(C, 70),
                C = R(C, 70),
                y = g * 0.85,
                z = d - y,
                J = d + y,
                M = Math.sqrt((1 - y * y / (g * g)) * l * l),
                y = e + M,
                M = s + M,
                K = e - 1;
                j.btnBorder = i.path(c.concat(["M", t, I], n(d, I, t, I, A, I, P, D, 0, 0), ["L", A, F], n(d, F, A, F, t, F, P, D, 0, 0), ["Z"])).attr({
                    "stroke-width": 4,
                    stroke: G(B, 80)
                }).add(j);
                j.btnBorder1 = i.path(c.concat(["M", p, I], n(d, I, p, I, u, I, g, l, 0, 0), ["L", u, F], n(d, F, u, F, p, F, g, l, 0, 0), ["Z"])).attr({
                    "stroke-width": 4,
                    stroke: G(B, 50)
                }).add(j);
                j.btnBorderLight = i.path(c.concat(["M", p, s], n(d, s, p, s, u, s, g, l, 0, 0), ["L", u, E], n(d, E, u, E, p, E, g, l, 1, 0), ["Z"])).attr({
                    "stroke-width": 0,
                    fill: {
                        FCcolor: {
                            color: o + q + B + q + o + q + o + q + B + q + H + q + B + q + o,
                            alpha: "50,50,50,50,50,70,50,50",
                            ratio: "0,15,0,12,0,15,43,15",
                            angle: 0
                        }
                    }
                }).add(j);
                j.back = i.path(c.concat(["M", p, s], n(d, s, p, s, u, s, g, l, 1, 0), ["L", u, e], n(d, e, u, e, p, e, g, l, 0, 0), ["Z"])).attr({
                    "stroke-width": 1,
                    stroke: G(B, 50),
                    fill: {
                        FCcolor: {
                            color: o + q + B + q + o + q + B + q + x + q + x + q + B + q + o,
                            alpha: "30,30,30,30,30,30,30,30",
                            ratio: "0,15,43,15,0,12,0,15",
                            angle: 0
                        }
                    }
                }).add(j);
                j.fluid = i.path(c.concat(["M", p, s], n(d, s, p, s, u, s, g, l, 0, 0), ["L", u, h], n(d, h, u, h, p, h, g, l, 1, 0), ["Z"])).attr({
                    "stroke-width": 0,
                    fill: {
                        FCcolor: {
                            gradientUnits: "objectBoundingBox",
                            cx: 0.5,
                            cy: 0,
                            r: "100%",
                            color: C + q + H,
                            alpha: "100,100",
                            ratio: "0,100",
                            radialGradient: !0
                        }
                    }
                }).add(j);
                j.fluidTop = i.path(c.concat(["M", w, h], n(d, h, w, h, r, h, v, l, 0, 0), ["L", r, h], n(d, h, r, h, w, h, v, l, 0, 0), ["Z"])).attr({
                    "stroke-width": 3,
                    stroke: G(C, 100),
                    fill: {
                        FCcolor: {
                            gradientUnits: "objectBoundingBox",
                            cx: 0.5,
                            cy: 0.7,
                            r: "100%",
                            color: C + q + H,
                            alpha: "100,100",
                            ratio: "0,100",
                            radialGradient: !0
                        }
                    }
                }).add(j);
                j.front = i.path(c.concat(["M", p, s], n(d, s, p, s, u, s, g, l, 0, 0), ["L", u, e], n(d, e, u, e, p, e, g, l, 1, 0), ["Z"])).attr({
                    "stroke-width": 1,
                    stroke: G(B, 50),
                    fill: {
                        FCcolor: {
                            color: o + q + B + q + o + q + o + q + B + q + o + q + B + q + o,
                            alpha: "30,30,30,30,30,30,30,30",
                            ratio: "0,15,0,12,0,15,43,15",
                            angle: 0
                        }
                    }
                }).add(j);
                j.frontLight = i.path(c.concat(["M", p, s], n(d, s, p, s, z, M, g, l, 0, 0), ["L", z, y], n(d, e, z, y, p, e, g, l, 1, 0), ["Z"])).attr({
                    "stroke-width": 0,
                    stroke: "#" + B,
                    fill: {
                        FCcolor: {
                            color: o + q + B,
                            alpha: "40,0",
                            ratio: "0,100",
                            angle: 0
                        }
                    }
                }).add(j);
                j.frontLight1 = i.path(c.concat(["M", J, M], n(d, s, J, M, u, s, g, l, 0, 0), ["L", u, e], n(d, e, u, e, J, y, g, l, 1, 0), ["Z"])).attr({
                    "stroke-width": 0,
                    stroke: "#" + B,
                    fill: {
                        FCcolor: {
                            color: o + q + B,
                            alpha: "40,0",
                            ratio: "0,100",
                            angle: 180
                        }
                    }
                }).add(j);
                j.cylinterTop = i.path(c.concat(["M", p, K], n(d, K, p, K, u, K, g, l, 0, 0), ["L", u, K], n(d, K, u, K, p, K, g, l, 0, 0), ["Z"])).attr({
                    "stroke-width": 2,
                    stroke: G(B, 40)
                }).add(j);
                return j
            }
        } (),
        x = Q.extendClass(N.bulb, {
            type: "cylinder",
            drawPoints: function() {
                var d = this.chart,
                c = this.options,
                a = c.max,
                b = c.min,
                k = this.data[0],
                e = (f(k.y, b) - b) / (a - b);
                k.minValue = b;
                k.maxValue = a;
                k.fulidHRatio = e;
                k.graphic = Za(c.cylOriginX, c.cylOriginY, c.cylRadius, c.cylHeight, c.cylYScale, "gStr", d.renderer, e, c.cylGlassColor, "100", c.cylFillColor, "100").add()
            },
            realtimeUpdate: function(d) {
                var c = this && this.data && this.data[0],
                a = this.chart,
                b = a.options.instanceAPI.numberFormatter,
                k = a.options.plotOptions.series.animation,
                e = {
                    duration: k ? d.interval: 0
                },
                g = d.values || [],
                h = d.labels || [],
                a = d.showLabels || [],
                d = d.toolTexts || [];
                if (c) {
                    var f = c.minValue,
                    j = c.maxValue,
                    i;
                    g[0] !== void 0 && (i = b.getCleanValue(g[0]), g = (i - f) / (j - f), g < 0 || g > 1 || (k && $(c.graphic).stop(!0, !0), c.graphic.animate({
                        fulidHRatio: g
                    },
                    e)));
                    b = m(h[0], b.dataLabels(i), "");
                    c.toolText = m(d[0], b);
                    a[0] == 0 && (b = "");
                    c.dataLabel.attr({
                        text: b
                    })
                }
            },
            drawDataLabels: function() {
                var d = this.chart,
                c = this.options,
                a = this.data[0],
                b = this.dataLabelsGroup,
                k,
                e = c.cylOriginY,
                g = c.cylOriginX;
                if (!b) b = this.dataLabelsGroup = d.renderer.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN
                }).add();
                k = c.cylinderTotalHeight + c.valuePadding + d.plotTop + c.labelLineHeight * 1;
                if (a.y !== void 0 && !isNaN(a.y)) a.dataLabel = d.renderer.text(a.displayValue, c.cylOriginX, k).attr({
                    align: "center"
                }).css(c.dataLabels.style).add(b);
                xa(g - c.cylRadius, e, c.cylRadius * 2, c.cylHeight, d.renderer, this, c.reverseScale)
            },
            animate: function() {
                var d, c, a = this.options.animation;
                a && !W(a) && (a = {});
                if ((c = (d = this.data[0]) && d.graphic) && !c.isAnimating) c.isAnimating = !0,
                c.attr({
                    fulidHRatio: 0
                }),
                c.animate({
                    fulidHRatio: d.fulidHRatio
                },
                a);
                this.animate = null
            },
            drawTracker: function() {}
        });
        N.cylinder = x;
        y.vbullet = S(y.bulb, {
            states: {
                hover: {}
            }
        });
        y.led = S(y.pie, {
            states: {
                hover: {}
            }
        });
        var $a = function() {
            var d = {
                value: !0
            },
            c = function(a, b) {
                var c, e, g = this,
                h = this._3dAttr,
                f;
                ea(a) && U(b) && (c = a, a = {},
                a[c] = b);
                if (ea(a)) g = d[a] ? g._3dAttr[a] : g._attr(a);
                else for (c in a) if (e = a[c], d[c]) {
                    if (e >= h.minValue && e <= h.maxValue) {
                        h[c] = e;
                        f = (e - h.minValue) / h.perLEDValueLength;
                        e = Math.round(f) * h.sizeGapSum - h.ledGap;
                        if (h.LEDCase) {
                            for (var j = g.colorArr,
                            i = 0,
                            m = j.length,
                            o, n, C, i = 0; i < m; i += 1) o = j[i],
                            o.maxLEDNoFrac < f ? C = h.LEDLowerFN: n ? C = h.LEDUpperFN: (C = void 0, n = o),
                            C && (o[C](), C === "show" && o.attr(o.oriShapeArg));
                            n.show();
                            n.attr(n.hoverShapeArg)
                        }
                        if (g.darkShade) {
                            f = {};
                            if (h.isXChange) {
                                if (f.width = h.w - e, h.isIncrement) f.x = h.x + e
                            } else if (f.height = h.h - e, h.isIncrement) f.y = h.y + e;
                            g.darkShade.attr(f)
                        }
                    }
                } else this._attr(c, e);
                return g
            };
            return function(a, b, d, e, g, h, l, j, i, p, o, n, C, v, s, u, w, r, t, A) {
                if (W(a)) b = a.y,
                d = a.w,
                e = a.h,
                g = a.gStr,
                h = a.renderer,
                l = a.value,
                j = a.gaugeFillColor,
                i = a.gaugeBorderColor,
                p = a.gaugeBorderAlpha,
                o = a.gaugeBorderThickness,
                n = a.colorRangeManager,
                C = a.minValue,
                v = a.maxValue,
                s = a.useSameFillColor,
                u = a.useSameFillBgColor,
                w = a.ledSize,
                r = a.ledGap,
                t = a.type,
                a = a.x;
                l >= C && l <= v || (l = C);
                var j = m(j, "FFFFFF"),
                i = m(i, "000000").replace(ra, sa),
                p = f(p, 1),
                o = m(o, "000000"),
                q = {
                    x: a,
                    y: b,
                    w: d,
                    h: e,
                    gStr: g,
                    renderer: h,
                    value: l,
                    gaugeFillColor: j,
                    gaugeBorderColor: i,
                    gaugeBorderAlpha: p,
                    gaugeBorderThickness: o,
                    colorRangeManager: n,
                    minValue: C,
                    maxValue: v,
                    ledGap: r,
                    ledSize: w,
                    type: t,
                    useSameFillColor: s,
                    useSameFillBgColor: u
                },
                g = h.g(g);
                g._attr = g.attr;
                g.attr = c;
                g.shadow = A;
                g._3dAttr = q;
                var n = n.getColorRangeArr(C, v),
                D = a,
                I = b,
                F = !0,
                E = !0,
                B = t === 2 || t === 4 ? e: d,
                y = r + w,
                x = B - w,
                z = v - C,
                v = 0,
                K = n.length,
                J = 0,
                M,
                L = J = 0,
                N = !1,
                R = "show",
                O = a,
                Q = b;
                s && (L += 1, LEDLOwerFN = "hide");
                u && (L += 2, R = "hide");
                x < 0 ? (s = 1, w = B) : (s = parseInt(x / y, 10) + 1, w += x % y / s, y = w + r);
                u = z / s;
                g.colorArr = [];
                B = [];
                t === 1 ? O += y - r / 2 : t === 2 ? (E = !1, Q += y - r / 2) : t === 3 ? (D = a + d, F = !1, O += y - r / 2) : (I = b + e, E = F = !1, Q += y - r / 2);
                q.ledGap = r;
                q.ledSize = w;
                q.sizeGapSum = y;
                q.perLEDValueLength = u;
                q.isIncrement = F;
                q.isXChange = E;
                q.LEDLowerFN = "show";
                q.LEDUpperFN = R;
                for ((q.LEDCase = L) && (L === 3 ? M = {
                    x: a,
                    y: b,
                    width: d,
                    height: e
                }: N = !0); v < K; v += 1) if ((w = n[v]) && U(w.maxvalue)) if (t = Math.round((w.maxvalue - C) / u), x = t - J, J = t, x > 0) {
                    q = {
                        r: 0
                    };
                    N && (M = {});
                    x *= y;
                    if (E) {
                        if (q.y = I, q.width = x - r, q.height = e, F ? (q.x = D, D += x) : (q.x = D - q.width, D -= x), N) M.width = q.x - a,
                        F && L === 1 || !F && L === 2 ? (M.x = a, M.width += q.width) : M.width = d - M.width
                    } else if (q.x = D, q.width = d, q.height = x - r, F ? (q.y = I, I += x) : (q.y = I - q.height, I -= x), N) M.height = q.y - b,
                    F && L === 1 || !F && L === 2 ? (M.y = b, M.height += q.height) : M.height = e - M.height;
                    x = h.rect(q).attr({
                        "stroke-width": 0,
                        fill: G(m(w.code, "000000"), f(w.alpha, 100))
                    }).add(g);
                    x.oriShapeArg = q;
                    x.hoverShapeArg = M;
                    x.maxLEDNo = t;
                    x.maxLEDNoFrac = (w.maxvalue - C) / u;
                    g.colorArr.push(x)
                }
                g.darkShade = h.rect(a, b, d, e, 0).attr({
                    "stroke-width": 0,
                    fill: G(j, 50)
                }).add(g);
                for (v = 1; v < s; v += 1) E ? (B.push("M", O, Q, "L", O, Q + e), O += y) : (B.push("M", O, Q, "L", O + d, Q), Q += y);
                g.LEDGap = h.path(B).attr({
                    stroke: G(j, 100),
                    "stroke-width": r
                }).add(g);
                g.border = h.path(["M", a, b, "L", a + d, b, a + d, b + e, a, b + e, "Z"]).attr({
                    stroke: G(i, p),
                    "stroke-width": o
                }).add(g).shadow(A, void 0, A);
                g.attr({
                    value: l
                });
                return g
            }
        } ();
        LED = Q.extendClass(N.pie, {
            type: "led",
            drawPoints: function() {
                var d = this.chart,
                c = this.options,
                a, b = c.gaugeOriginX,
                k = c.gaugeOriginY,
                e = c.max,
                g = c.min,
                h = this.data[0];
                a = this.chart.options.instanceAPI;
                var f = this.group,
                j = h.y;
                h.minValue = g;
                h.maxValue = e;
                a.isHorizontal ? (Ma(b, k + c.gaugeHeight, c.gaugeWidth, c.gaugeHeight, d.renderer, this, c.reverseScale), a = c.reverseScale ? 3 : 1) : (xa(b, k, c.gaugeWidth, c.gaugeHeight, d.renderer, this, c.reverseScale), a = c.reverseScale ? 2 : 4);
                h.graphic = $a(b, k, c.gaugeWidth, c.gaugeHeight, "group", d.renderer, j, c.gaugeFillColor, c.gaugeBorderColor, c.gaugeBorderAlpha, c.gaugeBorderThickness, c.colorRangeManager, g, e, c.useSameFillColor, c.useSameFillBgColor, c.ledSize, c.ledGap, a, c.shadow).add(f)
            },
            realtimeUpdate: function(d) {
                var c = this && this.data && this.data[0],
                a = this.chart.options.instanceAPI.numberFormatter,
                b = (this && this.chart).options.plotOptions.series.animation,
                k = {
                    duration: b ? d.interval: 0
                },
                e = d.labels || [],
                g = d.showLabels || [],
                h = d.toolTexts || [],
                d = a.getCleanValue((d.values || [])[0]);
                if (c) {
                    var f = c.maxValue;
                    if (d >= c.minValue && d <= f && (b && $(c.graphic).stop(!0, !0), c.graphic.animate({
                        value: d
                    },
                    k), b = m(e[0], a.dataLabels(d), ""), g[0] == 0 && (b = ""), c.dataLabel.attr({
                        text: b
                    }), a = m(h[0], a.dataLabels(d)), a !== void 0)) c.toolText = a
                }
            },
            drawDataLabels: function() {
                var d = this.chart,
                c = this.options,
                a = this.data[0],
                b = this.dataLabelsGroup,
                k,
                e = c.gaugeOriginX;
                k = c.gaugeOriginY;
                if (!b) b = this.dataLabelsGroup = d.renderer.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN
                }).add();
                k = c.gaugeHeight + c.valuePadding + k + c.tickBottomHeight + c.labelLineHeight * 1;
                if (a.y !== void 0 && !isNaN(a.y)) a.dataLabel = d.renderer.text(a.displayValue, e + c.gaugeWidth / 2, k).attr({
                    align: "center"
                }).css(c.dataLabels.style).add(b)
            },
            animate: function(d) {
                var c, a = this.options.animation;
                a && !W(a) && (a = {});
                if (!d) {
                    c = this.data[0];
                    var d = c.minValue,
                    b;
                    if (c = c && c.graphic) b = c.attr("value"),
                    c.attr({
                        value: d
                    }),
                    c.animate({
                        value: b
                    },
                    a)
                }
            }
        });
        N.led = LED;
        y.thermometer = S(y.bulb, {
            states: {
                hover: {}
            }
        });
        var ab = function() {
            var d = {
                fulidHRatio: !0,
                fluidColor: !0,
                fluidAlpha: !0,
                fluidFill: !0
            },
            c = [],
            a = function(b, a) {
                var e, g, h = this,
                l, j, i = !1,
                p = !1,
                o = this._3dAttr,
                n = o.renderer.getArcPath;
                ea(b) && U(a) && (e = b, b = {},
                b[e] = a);
                if (ea(b)) h = d[b] ? h._3dAttr[b] : h._attr(b);
                else for (e in b) if (g = b[e], d[e]) {
                    if (e === "fluidFill") g && g.linearGradient && g.stops && g.stops[0] && (g = g.stops[0][1]),
                    wa.test(g) ? (j = ua(g), l = j.get("hex"), j = j.get("a") * 100) : g && g.FCcolor ? (l = g.FCcolor.color.split(q)[0], j = g.FCcolor.alpha.split(q)[0]) : va.test(g) && (l = g.replace(ra, sa)),
                    o.fluidColor = m(l, o.fluidColor, "000000"),
                    o.fluidAlpha = f(j, o.fluidAlpha, 100),
                    i = !0;
                    else if (e === "fluidColor") o.fluidColor = m(g, o.fluidColor, "000000"),
                    i = !0;
                    else if (e === "fluidAlpha") o.fluidAlpha = f(g, o.fluidAlpha, 100),
                    i = !0;
                    else if (g >= 0 && g <= 1) o.fulidHRatio = g,
                    p = !0;
                    i && (g = L(o.fluidColor, o.is2D ? 80 : 70), h.fluid.attr({
                        fill: G(g, o.fluidAlpha)
                    }), h.fluidTop.attr({
                        fill: G(g, o.fluidAlpha)
                    }), h.topLight.attr({
                        stroke: G(g, o.fluidAlpha * 0.4)
                    }), h.topLightBorder.attr({
                        fill: {
                            FCcolor: {
                                color: g + q + g,
                                alpha: "0," + o.fluidAlpha * 0.3,
                                ratio: "0,80",
                                angle: 90
                            }
                        }
                    }));
                    p && (g = o.scaleY + o.h * (1 - o.fulidHRatio), h.fluid.attr({
                        d: o.fluidPath.concat(["L", o.lx2, g, o.lx1, g, "Z"])
                    }), h.fluidTop.attr({
                        d: c.concat(["M", o.lx1, g], n(o.x, g, o.lx1, g, o.lx2, g, o.lCylWidthHalf, 1, 1, 0), ["Z"])
                    }))
                } else this._attr(e, g);
                return h
            };
            return function(b, d, e, g, h, l, j, i, p, o, n, C, v, s) {
                if (W(b)) d = b.y,
                e = b.r,
                g = b.h,
                h = b.gStr,
                l = b.renderer,
                j = b.fulidHRatio,
                i = b.conColor,
                p = b.conBorderColor,
                o = b.conBorderThickness,
                n = b.fluidColor,
                C = b.fluidAlpha,
                v = b.is2D,
                b = b.x,
                s = b.showShadow;
                j >= 0 && j <= 1 || (j = 0);
                var i = m(i, "FFFFFF"),
                p = m(p, "#000000"),
                o = f(o, 1),
                n = m(n, "000000"),
                C = f(C, 100),
                u = {
                    x: b,
                    y: d,
                    r: e,
                    h: g,
                    renderer: l,
                    fulidHRatio: j,
                    conColor: i,
                    conBorderColor: p,
                    conBorderThickness: o,
                    fluidStroke: 3,
                    fluidColor: n,
                    is2D: v,
                    fluidAlpha: C
                },
                w = l.getArcPath,
                h = l.g(h);
                h._attr = h.attr;
                h.attr = a;
                h.shadow = s;
                h._3dAttr = u;
                var r = L(i, 80),
                n = L(n, v ? 80 : 70),
                i = R(i, 80),
                t = e * 0.643,
                A = t * 0.33,
                x = t - A,
                D = b - t,
                y = b + t,
                F = b - x,
                x = b + x,
                E = d + t,
                B = E + g,
                z = B + e * 0.766,
                H = d + A,
                g = E + g * (1 - j),
                j = t * 0.9,
                K = e + j - t,
                J = b - j,
                N = b + j,
                M = z - Math.abs(Math.sqrt(K * K - j * j)),
                O = parseInt(b - t * 0.6, 10),
                t = b + t / 2;
                u.fluidPath = c.concat(["M", J, M], w(b, z, J, M, N, M, K, K, 0, 1));
                u.scaleY = E;
                u.lx1 = J;
                u.lx2 = N;
                u.lCylWidthHalf = j;
                h.topLight = l.path(["M", J, E, "L", N, E]).attr({
                    "stroke-width": 1,
                    stroke: G(n, 40)
                }).add(h);
                h.topLightBorder = l.path(["M", J, E, "L", N, E, N, H, J, H, "Z"]).attr({
                    "stroke-width": 0,
                    fill: {
                        FCcolor: {
                            color: n + q + n,
                            alpha: "40,0",
                            ratio: "0,80",
                            radialGradient: !0,
                            gradientUnits: "objectBoundingBox",
                            cx: 0.5,
                            cy: 1,
                            r: "70%"
                        }
                    }
                }).add(h);
                h.fluid = l.path(u.fluidPath.concat(["L", N, g, J, g, "Z"])).attr({
                    "stroke-width": 0,
                    fill: G(n, C)
                }).add(h);
                h.fluidTop = l.path(c.concat(["M", J, g], w(b, g, J, g, N, g, j, 1, 1, 0), ["Z"])).attr({
                    "stroke-width": 0,
                    fill: G(n, C)
                }).add(h);
                h.border = l.path(c.concat(["M", F, d], w(F, H, F, d, D, H, A, A, 0, 0), ["L", D, B], w(b, z, D, B, y, B, e, e, 0, 1), ["L", y, H], w(x, H, y, H, x, d, A, A, 0, 0), ["Z"])).attr({
                    "stroke-width": o,
                    stroke: p
                }).add(h).shadow(s, void 0, s);
                if (!v) h.bulbBorderLight = l.path(c.concat(["M", D, B], w(b, z, D, B, y, B, e, e, 0, 1), w(b, z, y, B, D, B, e, e, 0, 0), w(b, z, D, B, y, B, e, e, 1, 0), ["Z"])).attr({
                    "stroke-width": 0,
                    stroke: "#00FF00",
                    fill: {
                        FCcolor: {
                            gradientUnits: "objectBoundingBox",
                            cx: 0.5,
                            cy: 0.5,
                            r: "50%",
                            color: r + q + i,
                            alpha: "0,50",
                            ratio: "78,30",
                            radialGradient: !0
                        }
                    }
                }).add(h),
                h.bulbTopLight = l.path(c.concat(["M", D, B], w(b, z, D, B, y, B, e, e, 0, 1), w(b, z, y, B, D, B, e, e, 0, 0), w(b, z, D, B, y, B, e, e, 1, 0), ["Z"])).attr({
                    "stroke-width": 0,
                    fill: {
                        FCcolor: {
                            gradientUnits: "objectBoundingBox",
                            cx: 0.3,
                            cy: 0.1,
                            r: "100%",
                            color: i + q + r,
                            alpha: "60,0",
                            ratio: "0,30",
                            radialGradient: !0
                        }
                    }
                }).add(h),
                h.bulbCenterLight = l.path(c.concat(["M", D, B], w(b, z, D, B, y, B, e, e, 0, 1), w(b, z, y, B, D, B, e, e, 0, 0), w(b, z, D, B, y, B, e, e, 1, 0), ["Z"])).attr({
                    "stroke-width": 0,
                    fill: {
                        FCcolor: {
                            gradientUnits: "objectBoundingBox",
                            cx: 0.25,
                            cy: 0.7,
                            r: "100%",
                            color: i + q + r,
                            alpha: "80,0",
                            ratio: "0,70",
                            radialGradient: !0
                        }
                    }
                }).add(h),
                h.cylLeftLight = l.path(c.concat(["M", b, d, "L", F, d], w(F, H, F, d, D, H, A, A, 0, 0), ["L", D, B, b, B, "Z"])).attr({
                    "stroke-width": 0,
                    fill: {
                        FCcolor: {
                            color: i + q + r,
                            alpha: "50,0",
                            ratio: "0,80",
                            angle: 0
                        }
                    }
                }).add(h),
                h.cylRightLight = l.path(c.concat(["M", D, d, "L", x, d], w(x, H, x, d, y, H, A, A, 1, 0), ["L", y, B, D, B, "Z"])).attr({
                    "stroke-width": 0,
                    fill: {
                        FCcolor: {
                            color: i + q + r + q + r,
                            alpha: "50,0, 0",
                            ratio: "0,40,60",
                            angle: 180
                        }
                    }
                }).add(h),
                h.cylLeftLight1 = l.path(["M", O, H, "L", D, H, D, B, O, B, "Z"]).attr({
                    "stroke-width": 0,
                    fill: {
                        FCcolor: {
                            color: i + q + r,
                            alpha: "60,0",
                            ratio: "0,100",
                            angle: 180
                        }
                    }
                }).add(h),
                h.cylRightLight1 = l.path(["M", O - 0.01, H, "L", t, H, t, B, O - 0.01, B, "Z"]).attr({
                    "stroke-width": 0,
                    fill: {
                        FCcolor: {
                            color: i + q + r,
                            alpha: "60,0",
                            ratio: "0,100",
                            angle: 0
                        }
                    }
                }).add(h);
                return h
            }
        } (),
        x = Q.extendClass(N.cylinder, {
            type: "thermometer",
            drawPoints: function() {
                var d = this.chart,
                c = this.options,
                a = c.max,
                b = c.min,
                k = this.data[0],
                e = (f(k.y, b) - b) / (a - b);
                k.minValue = b;
                k.maxValue = a;
                k.fulidHRatio = e;
                k.graphic = ab(c.thmOriginX, c.thmOriginY, c.thmBulbRadius, c.thmHeight, "gStr", d.renderer, e, c.thmGlassColor, c.gaugeBorderColor, c.gaugeBorderThickness, c.gaugeFillColor, c.gaugeFillAlpha, c.use3DLighting, c.shadow).add();
                a = c.thmOriginX - c.thmWidth / 2;
                b = c.thmWidth / 2;
                if (c.ticksOnRight) a = c.thmOriginX - c.thmWidth / 2,
                b = c.thmWidth;
                xa(a, c.thmOriginY + c.thmTopHeight, b, c.thmHeight, d.renderer, this, c.reverseScale)
            },
            drawDataLabels: function() {
                var d = this.chart,
                c = this.options,
                a = this.data[0],
                b = this.dataLabelsGroup,
                k,
                e = c.thmOriginX;
                if (!b) b = this.dataLabelsGroup = d.renderer.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN
                }).add();
                k = c.thmTotalHeight + c.valuePadding + d.plotTop + c.labelLineHeight * 1;
                if (a.y !== void 0 && !isNaN(a.y)) a.dataLabel = d.renderer.text(a.displayValue, e, k).attr({
                    align: "center"
                }).css(c.dataLabels.style).add(b)
            },
            animate: function() {
                var d, c, a = this.options.animation;
                a && !W(a) && (a = {});
                if ((c = (d = this.data[0]) && d.graphic) && !c.isAnimating) c.isAnimating = !0,
                c.attr({
                    fulidHRatio: 0
                }),
                c.animate({
                    fulidHRatio: d.fulidHRatio
                },
                a);
                this.animate = null
            }
        });
        N.thermometer = x;
        y.lineargauge = S(y.bulb, {
            states: {
                hover: {}
            }
        });
        x = Q.extendClass(N.bulb, {
            type: "lineargauge",
            drawTracker: function() {
                N.column.prototype.drawTracker.call(this)
            },
            realtimeUpdate: function(d) {
                if (d !== this.lastUpdatedObj) {
                    for (var c = this && this.options,
                    a = this && this.chart,
                    b = a.options.plotOptions.series.animation,
                    k = {
                        duration: b ? d.interval: 0
                    },
                    e = this && this.data, g = 0, h = this.chart.options.instanceAPI.numberFormatter, f, j, i = c.gaugeWidth, p = c.gaugeHeight, o = c.gaugeType, n = c.min, C = c.max, v = {
                        1 : function(b, a) {
                            return {
                                translateX: (b - a) * i / (C - n),
                                translateY: 0
                            }
                        },
                        2 : function(b, a) {
                            return {
                                translateX: (a - b) * i / (C - n),
                                translateY: 0
                            }
                        },
                        3 : function(b, a) {
                            return {
                                translateX: 0,
                                translateY: (b - a) * p / (C - n)
                            }
                        },
                        4 : function(b, a) {
                            return {
                                translateX: 0,
                                translateY: (a - b) * p / (C - n)
                            }
                        }
                    },
                    s = d.values || [], u = d.labels || [], w = d.toolTexts || [], r = d.showLabels || [], t = Math.min(e.length, s.length); g < t; g += 1) if (c = e[g], f = h.getCleanValue(s[g]), c && f <= C && f >= n && (j = v[o](s[g], c.value || n), b && $(c.graphic).stop(!0, !0), c.graphic.animate(j, k), f = m(u[g], h.dataLabels(s[g])), c.toolText = m(w[g], f), a.tooltip && a.tooltip.refresh(c, !0), f = r[g] == 0 ? "": f, c.dataLabel && (b && $(c.dataLabel).stop(!0, !0), c.dataLabel.animate(j, k), c.dataLabel.attr({
                        text: f
                    })), c.tracker)) b && $(c.tracker).stop(!0, !0),
                    c.tracker.animate(j, k),
                    c.plotX = c.origX + j.translateX,
                    c.plotY = c.origY + j.translateY;
                    this.lastUpdatedObj = d
                }
            },
            translate: function() {
                var d = this.data,
                c = this.chart,
                a = this.options,
                b = a.gaugeHeight,
                f = a.gaugeWidth,
                e = a.gaugeType,
                g = a.pointerOnOpp,
                h = a.gaugeOriginX - c.plotLeft,
                l = a.gaugeOriginY - c.plotTop,
                j = a.min,
                i = a.max,
                c = {
                    right: 0,
                    top: 0.5,
                    left: 1,
                    bottom: 1.5
                },
                a = d.length;
                e === 1 ? (e = function(a, c) {
                    return c === "bottom" ? {
                        x: h + a * f / (i - j),
                        y: l
                    }: {
                        x: h + a * f / (i - j),
                        y: l + b
                    }
                },
                g = g ? "top": "bottom") : e === 2 ? (e = function(a, c) {
                    return c === "bottom" ? {
                        x: f + h - a * f / (i - j),
                        y: l
                    }: {
                        x: f + h - a * f / (i - j),
                        y: l + b
                    }
                },
                g = g ? "top": "bottom") : (e = e === 3 ?
                function(a, c) {
                    return c === "right" ? {
                        x: h,
                        y: l + a * b / (i - j)
                    }: {
                        x: h + f,
                        y: l + a * b / (i - j)
                    }
                }: function(a, c) {
                    return c === "right" ? {
                        x: h,
                        y: b + l - a * b / (i - j)
                    }: {
                        x: h + f,
                        y: b + l - a * b / (i - j)
                    }
                },
                g = g ? "left": "right");
                for (; a--;) {
                    var m = d[a],
                    o = e((m.value || j) - j, g),
                    n = c[g] * Math.PI;
                    m.plotY = m.origY = o.y;
                    m.plotX = m.origX = o.x;
                    m.shapeType = "symbol";
                    m.shapeArgs = {
                        symbol: "poly_" + m.sides,
                        x: o.x,
                        y: o.y,
                        radius: m.radius,
                        options: {
                            startAngle: n
                        }
                    }
                }
            },
            drawPoints: function() {
                this.drawGraph();
                for (var d = this.data,
                c = d.length,
                a = this.chart.renderer,
                b = this.options.showPointerShadow,
                k, e; c--;) k = d[c],
                e = b ? {
                    opacity: Math.max(k.bgalpha, k.borderalpha) / 100
                }: null,
                k.graphic = a[k.shapeType](k.shapeArgs).attr({
                    fill: k.color,
                    stroke: k.borderColor,
                    "stroke-width": f(k.borderWidth, 1)
                }).add(this.group).shadow(e, this.group, e)
            },
            drawGraph: function() {
                var d = this.options,
                c = this.chart,
                a = d.gaugeType,
                b = d.gaugeOriginX,
                f = d.gaugeOriginY,
                e = d.gaugeHeight,
                g = d.gaugeWidth,
                h = d.min,
                l = d.max,
                j = d.colorRangeManager.getColorRangeArr(h, l),
                i = d.gaugeFillMix,
                m = d.gaugeFillRatio,
                o = d.gaugeBorderColor,
                n = d.gaugeBorderAlpha,
                q = d.showGaugeBorder ? d.gaugeBorderThickness: 0,
                v = c.renderer,
                s = c.options.instanceAPI,
                u = d.showShadow,
                w,
                r,
                t,
                A,
                y,
                x,
                z = s.colorM;
                a === 1 ? (w = function(a, c) {
                    return {
                        x: b + a * g / (l - h),
                        y: f,
                        width: (c - a) * g / (l - h),
                        height: e
                    }
                },
                r = 270) : a === 2 ? (w = function(a, c) {
                    return {
                        x: g + b - c * g / (l - h),
                        y: f,
                        width: (c - a) * g / (l - h),
                        height: e
                    }
                },
                r = 270) : (w = a === 3 ?
                function(a, c) {
                    return {
                        x: b,
                        y: f + a * e / (l - h),
                        width: g,
                        height: (c - a) * e / (l - h)
                    }
                }: function(a, c) {
                    return {
                        x: b,
                        y: e + f - c * e / (l - h),
                        width: g,
                        height: (c - a) * e / (l - h)
                    }
                },
                r = 180);
                var F = v.g("linearGauge");
                F.shadow = function() {};
                F.outerRect = v.rect(b, f, g, e, 0).add(F);
                for (var E = 0,
                B = j.length; E < B; E += 1) A = j[E],
                x = w(A.minvalue - h, A.maxvalue - h),
                A.x = x.x,
                A.y = x.y,
                A.width = x.width,
                A.height = x.height,
                t = A.code,
                y = G(Va(t, o), n),
                a = u ? {
                    opacity: Math.max(A.alpha, n) / 100
                }: null,
                F["colorBand" + E] = v.rect(x.x, x.y, x.width, x.height, 0).attr({
                    "stroke-width": q,
                    stroke: y,
                    fill: {
                        FCcolor: {
                            color: z.parseColorMix(t, i).toString(),
                            ratio: m,
                            alpha: A.alpha,
                            angle: r
                        }
                    }
                }).add(F).shadow(a, void 0, a);
                F.add();
                s.isHorizontal ? Ma(b, f + d.gaugeHeight, d.gaugeWidth, d.gaugeHeight, c.renderer, this, d.reverseScale) : xa(b, f, d.gaugeWidth, d.gaugeHeight, c.renderer, this, d.reverseScale)
            },
            drawDataLabels: function() {
                var d = this.data,
                c = this.chart,
                a = this.options,
                b = this.dataLabelsGroup,
                k = a.max,
                e = a.min,
                g = a.colorRangeManager.getColorRangeArr(e, k),
                h = a.gaugeWidth,
                l = a.gaugeHeight,
                j = a.gaugeOriginX - c.plotLeft,
                i = a.gaugeOriginY - c.plotTop,
                m = a.gaugeType,
                o = a.pointerOnOpp,
                n = a.valueInsideGauge,
                q = a.showGaugeLabels,
                v,
                s,
                u,
                w,
                r,
                t = a.dataLabels.style,
                A = c.renderer.smartLabel,
                x,
                y = f(parseInt(t.fontHeight, 10), parseInt(t.lineHeight, 10), 12),
                z = a.valuePadding + y,
                z = n === o ? z: z + y / 2;
                A.setStyle(t);
                x = A.getOriSize("W").width;
                if (!b) b = this.dataLabelsGroup = c.renderer.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN
                }).add(this.group);
                m === 1 ? (u = function(a, b, c) {
                    return {
                        x: j + a * h / (k - e),
                        y: c ? b ? i + l - z: i + l + z: b ? i + z: i - z,
                        align: "center"
                    }
                },
                w = function(a, b) {
                    return {
                        x: j + (a - e + (b - a) / 2) * h / (k - e),
                        y: i + l / 2,
                        width: (b - a) * h / (k - e),
                        height: l
                    }
                }) : m === 2 ? (u = function(a, b, c) {
                    return {
                        x: h + j - a * h / (k - e),
                        y: c ? b ? i + l - z: i + l + z: b ? i + z: i - z,
                        align: "center"
                    }
                },
                w = function(a, b) {
                    return {
                        x: j + h - (a - e + (b - a) / 2) * h / (k - e),
                        y: i + l / 2,
                        width: (b - a) * h / (k - e),
                        height: l
                    }
                }) : m === 3 ? (u = function(a, b, c) {
                    c ? b ? (c = "right", b = j + h - z) : (c = "left", b = j + h + z) : b ? (c = "left", b = j + z) : (c = "right", b = j - z);
                    return {
                        x: b,
                        y: i + a * l / (k - e),
                        align: c
                    }
                },
                w = function(a, b) {
                    return {
                        y: i + (a - e + (b - a) / 2) * l / (k - e),
                        x: j + h / 2,
                        height: (b - a) * l / (k - e),
                        width: h
                    }
                }) : (u = function(a, b, c) {
                    c ? b ? (c = "right", b = j + h - z) : (c = "left", b = j + h + z) : b ? (c = "left", b = j + z) : (c = "right", b = j - z);
                    return {
                        x: b,
                        y: l + i - a * l / (k - e),
                        align: c
                    }
                },
                w = function(b, a) {
                    return {
                        y: i + l - (b - e + (a - b) / 2) * l / (k - e),
                        x: j + h / 2,
                        height: (a - b) * l / (k - e),
                        width: h
                    }
                });
                if (d) {
                    m = 0;
                    for (v = d.length; m < v; m += 1) if (s = d[m], s.showvalue != 0 && s.displayValue != J) r = u(s.value - e, n, o),
                    s.dataLabel = c.renderer.text(s.displayvalue, r.x, r.y).attr({
                        align: r.align
                    }).css(t).add(b)
                }
                if (g && q) {
                    m = 0;
                    for (v = g.length; m < v; m += 1) d = g[m],
                    r = w(d.minvalue, d.maxvalue),
                    d = r.width - 4 > x && r.height - 4 > y ? A.getSmartText(d.label, r.width - 4, r.height - 4) : A.getSmartText(d.label, r.width, r.height),
                    c.renderer.text(d.text, r.x, r.y + y / 4 - (d.height - y) / 2).attr({
                        align: "center"
                    }).css(a.tickValueStyle).add(b)
                }
            },
            animate: function() {
                var d = 0,
                c = this.options,
                a = this.data.length,
                b = this.chart,
                f = c.animation,
                e = c.gaugeType,
                g = c.gaugeWidth,
                h = c.gaugeHeight,
                l = c.gaugeOriginX - b.plotLeft,
                j = c.gaugeOriginY - b.plotTop,
                i = e > 2 ? "translateY": "translateX",
                m = {},
                o = {},
                n = {
                    1 : function(b) {
                        return l - b.shapeArgs.x
                    },
                    2 : function(b) {
                        return l + g - b.shapeArgs.x
                    },
                    3 : function(b) {
                        return j - b.shapeArgs.y
                    },
                    4 : function(b) {
                        return j + h - b.shapeArgs.y
                    }
                };
                f && !W(f) && (f = {});
                for (o[i] = 0; d < a; d += 1) if (b = (c = this.data[d]) && c.graphic, m[i] = n[e](c), b && !b.isAnimating) b.isAnimating = !0,
                b.attr(m),
                b.animate(o, f);
                this.animate = null
            }
        });
        N.lineargauge = x;
        y.hbullet = S(y.lineargauge, {
            states: {
                hover: {}
            }
        });
        x = Q.extendClass(N.lineargauge, {
            type: "hbullet",
            realtimeUpdate: function(d) {
                var c = this && this.options,
                a = this && this.data,
                b = this.chart,
                f, e = b.options.instanceAPI.numberFormatter,
                g, h, l = c.gaugeWidth,
                b = c.gaugeOriginX - b.plotLeft,
                j = c.min,
                i, c = c.max;
                g = d.values || [];
                if (a[0] && g[0] && (d = a[0], f = g[0], g = e.getCleanValue(g[0], !0), d && g <= c && g >= j)) d.plotasdot ? (i = b + (g - j) * l / (c - j) - d.shapeArgs.width / 2, d.graphic.animate({
                    x: i
                })) : (h = (g - j) * l / (c - j), i = (g - d.value) * l / (c - j), d.graphic.animate({
                    width: h
                })),
                f = m(f.label, e.dataLabels(g)),
                d.toolText = f,
                d.value = g,
                d.dataLabel && d.dataLabel.attr({
                    text: f
                }),
                d.tracker && (d.tracker.animate({
                    width: h
                }), d.plotX += i / 2);
                if (a[1] && value[1] && (d = a[1], f = value[1], g = e.getCleanValue(W(f) ? f.value: f, !0), d && g <= c && g >= j && (i = b + (g - j) * l / (c - j) - d.shapeArgs.width / 2, d.graphic.animate({
                    x: i
                }), f = m(f.label, e.dataLabels(g)), d.toolText = f, d.value = g, d.tracker))) d.tracker.animate({
                    x: i
                }),
                d.plotX = i
            },
            drawPoints: function() {
                this.drawGraph();
                var d = this.data,
                c = this.options,
                a = this.chart,
                b = a.renderer,
                f = c.gaugeHeight,
                e = c.gaugeWidth,
                g = c.gaugeOriginX - a.plotLeft,
                a = c.gaugeOriginY - a.plotTop,
                h = c.max,
                l = c.min,
                j = d[0],
                d = d[1],
                i,
                m,
                o,
                n,
                q,
                c = d.targetthickness;
                if (!j.isValueUndef) i = e * (j.value - l) / (h - l),
                j.plotasdot ? (n = q = 10, i -= q / 2, o = i) : (i = e * ((l <= 0 && 0 <= h ? 0 : l < 0 ? h: l) - l) / (h - l), o = e * (j.value - l) / (h - l), j.value >= 0 ? (q = o - i, o = i) : (o = i + o, i = o - i, o -= i, q = o - i), n = f * (j.plotfillpercent / 100)),
                m = (f - n) / 2,
                j.shapeType = "rect",
                j.plotX = g + i + q / 2,
                j.plotY = a + m - 10,
                j.shapeArgs = {
                    x: g + i,
                    y: a + m,
                    height: n,
                    width: q,
                    endX: g + o,
                    r: 0
                },
                j.graphic = b[j.shapeType](j.shapeArgs).attr({
                    fill: j.color,
                    stroke: j.borderColor,
                    "stroke-width": j.borderWidth
                }).add(this.group);
                if (!d.isValueUndef) e = e * (d.value - l) / (h - l),
                i = e - c / 2,
                e = f * d.targetfillpercent / 100,
                m = (f - e) / 2,
                d.plotX = g + i + c / 2,
                d.plotY = a + m - 10,
                d.shapeType = "rect",
                d.shapeArgs = {
                    x: g + i,
                    y: a + m,
                    height: e,
                    width: c,
                    r: 0
                },
                d.graphic = b[d.shapeType](d.shapeArgs).attr({
                    fill: d.color
                }).add(this.group)
            },
            drawDataLabels: function() {
                var d = this.data,
                c = this.chart,
                a = this.options,
                b = this.dataLabelsGroup,
                k = a.showValue,
                e = a.gaugeOriginX - c.plotLeft,
                g = a.gaugeWidth,
                h = a.gaugeHeight,
                l = a.dataLabels.style,
                j = f(parseInt(l.fontHeight, 10), parseInt(l.lineHeight, 10), 12),
                a = a.valuePadding;
                if (!b) b = this.dataLabelsGroup = c.renderer.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN
                }).add(this.group);
                if ((d = d[0]) && k && d.displayValue != J) d.dataLabel = c.renderer.text(d.displayvalue, e + g + a, (h + j) / 2).attr({
                    align: "left"
                }).css(l).add(b)
            },
            animate: function() {
                var d = this.data,
                c = this.options,
                a, b = c.animation,
                f = c.gaugeWidth,
                e = c.gaugeOriginX - this.chart.plotLeft;
                b && !W(b) && (b = {});
                if (c = d[0]) if ((a = c && c.graphic) && !a.isAnimating) a.isAnimating = !0,
                a.attr({
                    width: 0,
                    x: c.shapeArgs.endX
                }),
                a.animate({
                    width: c.shapeArgs.width,
                    x: c.shapeArgs.x
                },
                b);
                if (c = d[1]) if ((a = c && c.graphic) && !a.isAnimating) a.isAnimating = !0,
                a.attr({
                    x: e + f - c.shapeArgs.width
                }),
                a.animate({
                    x: c.shapeArgs.x
                },
                b);
                this.animate = null
            }
        });
        N.hbullet = x;
        var bb = function(d, c) {
            var a = this.series && this.series[0],
            b = a && a.options,
            f = a && a.data,
            e,
            g = this.options.instanceAPI.numberFormatter,
            h,
            l,
            j = b.gaugeHeight,
            a = b.gaugeOriginY - a.chart.plotTop,
            i = b.min,
            p = b.max;
            d instanceof Array || (d = [{
                value: d,
                label: c
            }]);
            if (f[0] && d[0] && (b = f[0], e = d[0], h = g.getCleanValue(W(e) ? e.value: e, !0), b && h <= p && h >= i && (b.plotasdot ? (l = (h - i) * j / (p - i), b.graphic.animate({
                y: a + j - l - b.shapeArgs.height / 2
            })) : (l = (h - i) * j / (p - i), b.graphic.animate({
                height: l,
                y: a + j - l
            })), e = m(e.label, g.dataLabels(h)), b.toolText = e, b.value = h, b.dataLabel && b.dataLabel.attr({
                text: e
            }), b.tracker))) b.tracker.animate({
                height: l,
                y: a + j - l
            }),
            b.plotY = a + j - l / 2;
            if (f[1] && d[1] && (b = f[1], e = d[1], h = g.getCleanValue(W(e) ? e.value: e, !0), b && h <= p && h >= i && (l = (h - i) * j / (p - i), b.graphic.animate({
                y: a + j - l - b.shapeArgs.height / 2
            }), e = m(e.label, g.dataLabels(h)), b.toolText = e, b.value = h, b.tracker))) b.tracker.animate({
                y: a + j - l - b.shapeArgs.height / 2
            }),
            b.plotY = a + j - l
        };
        y.vbullet = S(y.lineargauge, {
            states: {
                hover: {}
            }
        });
        x = Q.extendClass(N.lineargauge, {
            type: "vbullet",
            drawPoints: function() {
                this.drawGraph();
                var d = this.data,
                c = this.options,
                a = this.chart,
                b = a.renderer,
                k = c.gaugeHeight,
                e = c.gaugeWidth,
                g = c.gaugeOriginX - a.plotLeft,
                h = c.gaugeOriginY - a.plotTop,
                l = c.max,
                j = c.min,
                i = d[0],
                d = d[1],
                m,
                o,
                n,
                q,
                c = d.targetthickness;
                a.realTimeUpdate = bb;
                if (!i.isValueUndef) a = k * (l - i.value) / (l - j),
                i.plotasdot ? (n = q = 10, o = m = a - n / 2) : (m = k * (l - (j <= 0 && 0 <= l ? 0 : j < 0 ? l: j)) / (l - j), o = k * (l - i.value) / (l - j), i.value <= 0 ? (n = o - m, o = m) : (o = m + o, m = o - m, o -= m, n = o - m), q = e * (i.plotfillpercent / 100)),
                a = (e - q) / 2,
                i.shapeType = "rect",
                i.plotX = a,
                i.plotY = m + n / 2,
                i.shapeArgs = {
                    x: g + a,
                    y: h + m,
                    height: n,
                    width: q,
                    endY: h + o,
                    r: 0
                },
                i.graphic = b[i.shapeType](i.shapeArgs).attr({
                    fill: G(i.color, i.plotfillalpha),
                    stroke: G(i.borderColor, i.borderalpha),
                    "stroke-width": i.showplotborder ? f(i.borderWidth, 1) : 0
                }).add(this.group);
                if (!d.isValueUndef) l = k * (d.value - j) / (l - j),
                m = h + (k - (l + c / 2)),
                k = e * d.targetfillpercent / 100,
                a = g + (e - k) / 2,
                d.plotX = a,
                d.plotY = m + c / 2,
                d.shapeType = "rect",
                d.shapeArgs = {
                    x: a,
                    y: m,
                    height: c,
                    width: k,
                    r: 0
                },
                d.graphic = b[d.shapeType](d.shapeArgs).attr({
                    fill: d.color
                }).add(this.group)
            },
            drawDataLabels: function() {
                var d = this.data,
                c = this.chart,
                a = this.options,
                b = this.dataLabelsGroup,
                k = a.showValue,
                e = a.gaugeOriginX - c.plotLeft,
                g = a.gaugeWidth,
                h = a.gaugeHeight,
                l = a.dataLabels.style,
                j = f(parseInt(l.fontHeight, 10), parseInt(l.lineHeight, 10), 12),
                i = c.renderer.smartLabel,
                a = a.valuePadding;
                if (!b) b = this.dataLabelsGroup = c.renderer.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN
                }).add(this.group);
                if ((d = d[0]) && k && d.displayValue != J) l ? i.setStyle(l) : l = {},
                d.dataLabel = c.renderer.text(d.displayvalue, e + g / 2, h + j + a).attr({
                    align: "center"
                }).css(l).add(b)
            },
            animate: function() {
                var d = this.data,
                c = this.options,
                a, b = c.animation,
                f = c.gaugeOriginY - this.chart.plotTop;
                b && !W(b) && (b = {});
                if (c = d[0]) if ((a = c && c.graphic) && !a.isAnimating) a.isAnimating = !0,
                a.attr({
                    height: 0,
                    y: c.shapeArgs.endY
                }),
                a.animate({
                    height: c.shapeArgs.height,
                    y: c.shapeArgs.y
                },
                b);
                if (c = d[1]) if ((a = c && c.graphic) && !a.isAnimating) a.isAnimating = !0,
                a.attr({
                    y: f
                }),
                a.animate({
                    y: c.shapeArgs.y
                },
                b);
                this.animate = null
            }
        });
        N.vbullet = x;
        y.drawingpad = S(y.pie, {
            states: {
                hover: {}
            }
        });
        x = Q.extendClass(N.pie, {
            type: "drawingpad",
            drawPoints: function() {},
            drawDataLabel: function() {},
            animate: function() {}
        });
        N.drawingpad = x;
        y.sparkline = S(y.line, {
            states: {
                hover: {}
            }
        });
        x = Q.extendClass(N.line, {
            type: "sparkline",
            drawDataLabels: function() {
                var d = this.chart,
                c = this.options,
                a = this.data[0],
                b = this.dataLabelsGroup,
                f = c.dataLabels.style,
                e = T({},
                f);
                if (!b) b = this.dataLabelsGroup = d.renderer.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN
                }).translate(d.plotLeft, d.plotTop).add(d.seriesGroup);
                if (typeof c.openValue != "undefined") e.color = c.openColor,
                a.dataLabel = d.renderer.text(c.openValue, c.openValueX, c.dataLabelY).attr({
                    align: "right"
                }).css(e).add(b);
                if (typeof c.closeValue != "undefined") e.color = c.closeColor,
                a.dataLabel = d.renderer.text(c.closeValue, c.closeValueX, c.dataLabelY).attr({
                    align: "left"
                }).css(e).add(b),
                a.dataLabel.getBBox();
                if (typeof c.maxDataValue != "undefined") a.dataLabel = d.renderer.text('[<span style="color: ' + c.highColor + '">' + c.maxDataValue + '</span><span>|</span><span style="color: ' + c.lowColor + '">' + c.minDataValue + "</span>]", c.highLowValueX, c.dataLabelY, "0").attr({
                    align: "left"
                }).css(f).add(b)
            }
        });
        N.sparkline = x;
        y.sparkwinloss = S(y.column, {
            states: {
                hover: {}
            }
        });
        x = Q.extendClass(N.column, {
            type: "sparkwinloss",
            drawDataLabels: function() {
                var d = this.chart,
                c = this.options,
                a = this.data[0],
                b = this.dataLabelsGroup,
                f = T({},
                c.dataLabels.style);
                if (!b) b = this.dataLabelsGroup = d.renderer.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN
                }).translate(d.plotLeft, d.plotTop).add(d.seriesGroup);
                if (typeof c.labelText != "undefined") f.color = c.openColor,
                a.dataLabel = d.renderer.text(c.labelText, c.dataLabelX, c.dataLabelY).attr({
                    align: "left"
                }).css(f).add(b)
            }
        });
        N.sparkwinloss = x;
        y.angulargauge = S(y.pie, {
            states: {
                hover: {}
            }
        });
        var Fa = function(d, c) {
            var a = Math.pow(10, c == void 0 ? 2 : c);
            d *= a;
            d = Math.round(Number(String(d)));
            d /= a;
            return d
        },
        za = function(d, c, a, b) {
            return [Fa(d * a - c * b, 2), Fa(c * a + d * b, 2)]
        },
        cb = function() {
            return function(d, c) {
                var a, b, k, e = this,
                g = this._Attr,
                h, l, j;
                g || (g = e._Attr = {});
                ea(d) && U(c) && (a = d, d = {},
                d[a] = c);
                if (ea(d)) e = d == "angle" ? e._Attr[d] : e._attr(d);
                else for (a in d) if (b = d[a], a == "angle") if (g[a] = b, l = b * ka, g.tooltipPos[0] = g.cx + g.toolTipRadius * Math.cos(l), g.tooltipPos[1] = g.cy + g.toolTipRadius * Math.sin(l), e.renderer.type === "VML") {
                    k = e.element;
                    h = m(g.d, e.d.split(" "));
                    g.d = h;
                    j = Math.cos(l);
                    l = Math.sin(l);
                    h = ["M"].concat(za(h[1], h[2], j, l), ["L"], za(h[4], h[5], j, l), za(h[6], h[7], j, l), za(h[8], h[9], j, l), ["Z"]);
                    e._attr({
                        d: h
                    });
                    if (g.color && g.color.FCcolor) g.color.FCcolor.angle = 0 - b;
                    oa(k.childNodes,
                    function(a) {
                        var c;
                        if (U(a.angle)) c = f(a.angle, 0),
                        U(a.FCColorAngle) ? c = a.FCColorAngle: a.FCColorAngle = c,
                        c -= b,
                        Ua ? a.angle = c: ta(a, "angle", c)
                    })
                } else e.rotate(b, 0, 0);
                else e._attr(a, b);
                return e
            }
        },
        Q = Q.extendClass(N.pie, {
            type: "angulargauge",
            drawPoints: function() {
                for (var d = this.data,
                c = this.options,
                a = this.chart.renderer,
                b = Number(c.gaugeOriginX), k = Number(c.gaugeOriginY), e = c.gaugeStartAngle, g = c.gaugeEndAngle, h = c.min, l = c.max, j = this.pointGroup, i = l - h, m = g - e, o = 0, n, q, v, s, u = d && d.length, w, r = cb(e, g); o < u; o += 1) if (g = d[o], U(g.y)) n = f(g.radius, (Number(c.gaugeOuterRadius) + Number(c.gaugeInnerRadius)) / 2),
                q = g.baseWidth,
                q /= 2,
                v = g.topWidth,
                s = v / 2,
                v = g.rearExtension,
                w = g.y >= h && g.y <= l ? (e + (g.y - h) / i * m) / ka: e,
                g.tooltipPos = [b, k],
                g.graphic = a.path(["M", n, -s, "L", n, s, -v, q, -v, -q, "Z"]).attr({
                    fill: g.color,
                    stroke: g.borderColor,
                    "stroke-width": g.borderThickness
                }).add(j).shadow(c.shadow),
                g.graphic._attr = g.graphic.attr,
                g.graphic.attr = r,
                g.graphic._Attr = {
                    tooltipPos: g.tooltipPos,
                    cx: b,
                    cy: k,
                    toolTipRadius: n - v,
                    color: g.color
                },
                g.graphic.attr({
                    angle: w
                })
            },
            drawGraph: function() {
                var d = this.options,
                c = this.chart.renderer,
                a = d.gaugeOuterRadius,
                b = d.gaugeInnerRadius,
                f = d.gaugeFillRatio,
                e = d.showGaugeBorder,
                g = d.gaugeBorderColor,
                h = d.gaugeBorderThickness,
                l = d.gaugeBorderAlpha,
                j = d.gaugeFillMix,
                i = Number(d.gaugeOriginX),
                p = Number(d.gaugeOriginY),
                o = d.gaugeStartAngle,
                n = d.gaugeEndAngle,
                q = d.min,
                v = d.max,
                s = d.colorRangeManager.getColorRangeArr(q, v),
                u = 0,
                w = s.length,
                r = v - q,
                t = n - o,
                x,
                y,
                z,
                I = n > o ? 1 : 0,
                F = 1 - I;
                z = o;
                var E = Math.cos(o),
                B = Math.sin(o);
                y = i + a * E;
                for (var J = p + a * B,
                H = i + b * E,
                K = p + b * B,
                L, N, M = this.chart.options.instanceAPI; u < w; u += 1) {
                    n = s[u];
                    x = o + (Math.min(n.maxvalue, v) - q) / r * t;
                    E = Math.cos(x);
                    B = Math.sin(x);
                    L = i + a * E;
                    N = p + a * B;
                    E = i + b * E;
                    B = p + b * B;
                    z = Math.abs(z - x) > Math.PI ? 1 : 0;
                    y = ["M", y, J].concat(c.getArcPath(i, p, y, J, L, N, a, a, I, z), ["L", E, B], c.getArcPath(i, p, E, B, H, K, b, b, F, z), ["Z"]);
                    J = M.parseColorMix(n.code, j);
                    H = M.parseAlphaList(n.alpha, J.length);
                    K = M.parseRatioList(b / a * 100 + f, J.length);
                    z = n.bordercolor;
                    var O = m(n.borderAlpha, e == 1 ? l: ga);
                    z = z && z.indexOf("{") == -1 ? G(z, O) : M.parseColorMix(n.code, m(z, g))[0];
                    z = G(z, O);
                    c.path(y).attr({
                        fill: {
                            FCcolor: {
                                gradientUnits: "userSpaceOnUse",
                                cx: i,
                                cy: p,
                                r: a,
                                color: J.join(),
                                alpha: H,
                                ratio: K,
                                defaultColor: n.color,
                                defaultAlpha: n.alpha,
                                radialGradient: !0
                            }
                        },
                        "stroke-width": h,
                        stroke: z
                    }).add(this.group);
                    y = L;
                    J = N;
                    H = E;
                    K = B;
                    z = x
                }
                if (!this.tickMarkGroup) this.tickMarkGroup = c.g("tickMark").add(this.group);
                if (!this.pointGroup) this.pointGroup = c.g("point").translate(i, p).add(this.group);
                if (!this.pointGroup) this.pointGroup = c.g("point").translate(i, p).add(this.group);
                c.circle(i, p, d.pivotRadius).attr({
                    fill: {
                        FCcolor: {
                            color: d.pivotFillColor,
                            alpha: d.pivotFillAlpha,
                            ratio: d.pivotFillRatio,
                            radialGradient: d.isRadialGradient,
                            gradientUnits: "objectBoundingBox",
                            cx: 0.5,
                            cy: 0.5,
                            r: "50%"
                        }
                    },
                    "stroke-width": d.pivotBorderThickness,
                    stroke: d.pivotBorderColor
                }).add(this.group)
            },
            drawTracker: function() {
                var d = this,
                c = d.chart,
                a, b = +new Date,
                f;
                oa(d.data,
                function(e) {
                    a = e.trackerEventAdded;
                    if (e.y !== null && !a && e.graphic) {
                        if (e.link !== void 0 || e.editMode) var g = {
                            cursor: "pointer",
                            _cursor: "hand"
                        };
                        e.graphic.attr({
                            isTracker: b
                        }).on(Ia ? "touchstart": "mouseover",
                        function(a) {
                            f = a.relatedTarget || a.fromElement;
                            if (c.hoverSeries !== d && ta(f, "isTracker") !== b) d.onMouseOver();
                            e.onMouseOver()
                        }).on("mouseout",
                        function(a) {
                            if (!d.options.stickyTracking && (f = a.relatedTarget || a.toElement, ta(f, "isTracker") !== b)) d.onMouseOut()
                        }).css(g);
                        e.trackerEventAdded = !0
                    }
                })
            },
            drawTickMarks: function() {
                var d = this.options,
                c = this.chart.renderer,
                a = Number(d.gaugeOriginX),
                b = Number(d.gaugeOriginY),
                f = d.gaugeStartAngle,
                e = d.min,
                g = Number(d.gaugeInnerRadius),
                h = Number(d.gaugeOuterRadius),
                l = d.max - e,
                j = d.gaugeEndAngle - f,
                i = 0,
                m = d.majorTM,
                o = d.minorTM,
                n = this.tickMarkGroup,
                q,
                v,
                s,
                u,
                w,
                r,
                t = Math.cos,
                x = Math.sin,
                y = Number(d.minorTMHeight),
                z = Number(d.majorTMHeight),
                i = d.placeValuesInside;
                w = d.tickValueDistance;
                var I, F;
                d.placeTicksInside ? (I = g, y = I + y, z = I + z) : (I = h, y = I - y, z = I - z);
                F = i ? g - w: h + w;
                if (!this.majorTM) this.majorTM = [];
                if (!this.tmLabel) this.tmLabel = [];
                i = 0;
                for (g = m.length; i < g; i += 1) h = m[i],
                q = h.value,
                w = h.displayValue,
                r = (q - e) * j / l + f,
                q = a + I * t(r),
                v = b + I * x(r),
                s = a + z * t(r),
                u = b + z * x(r),
                this.majorTM[i] = c.path(["M", q, v, "L", s, u]).attr({
                    stroke: G(d.majorTMColor, d.majorTMAlpha),
                    "stroke-width": d.majorTMThickness
                }).add(n),
                w !== "" && (q = a + F * t(r) + (h.x || 0), v = b + F * x(r) + (h.y || 0), this.tmLabel[i] = c.text(w, q, v).attr({
                    align: "center"
                }).css(d.tickValueStyle).add(n));
                if (!this.minorTM) this.minorTM = [];
                i = 0;
                for (g = o.length; i < g; i += 1) q = o[i],
                r = (q - e) * j / l + f,
                q = a + I * t(r),
                v = b + I * x(r),
                s = a + y * t(r),
                u = b + y * x(r),
                this.majorTM[i] = c.path(["M", q, v, "L", s, u]).attr({
                    stroke: G(d.minorTMColor, d.minorTMAlpha),
                    "stroke-width": d.minorTMThickness
                }).add(n)
            },
            drawDataLabels: function() {
                var d = this.data,
                c = this.chart,
                a = this.options,
                b = this.dataLabelsGroup,
                k, e, g = c.renderer,
                h = a.dataLabels.style,
                l = a.pivotRadius,
                j = f(parseInt(h.lineHeight, 10), 12),
                i = a.valueBelowPivot,
                m = a.gaugeOriginX,
                o = a.gaugeOriginY + (i ? j + l + 2 : -l - 2);
                if (!b) b = this.dataLabelsGroup = g.g("data-labels").attr({
                    visibility: this.visible ? ba: HIDDEN,
                    zIndex: 6
                }).translate(c.plotLeft, c.plotTop).add(),
                c.options.chart.hasScroll && b.clip(this.clipRect);
                oa(d,
                function(a) {
                    if (a.displayValue != "") e = a.valueY,
                    k = f(a.valueX, m),
                    U(e) || (e = o, o += i ? j: -j),
                    a.dataLabel = g.text(a.displayValue, k, e).attr({
                        align: "center"
                    }).css(h).add(b)
                })
            },
            realtimeUpdate: function(d) {
                for (var c = this && this.options,
                a = this && this.data,
                b = this && this.chart,
                f = 0,
                e = b.options.instanceAPI.numberFormatter,
                b = b.options.plotOptions.series.animation ? d.interval: 0, g, h, l = c.gaugeStartAngle, j = c.min, i = c.max, p = i - j, c = c.gaugeEndAngle - l, o = d.values || [], n = d.labels || [], q = d.showLabels || [], v = d.toolTexts || [], s = Math.min(o.length, a.length); f < s; f += 1) if (d = a[f], h = e.getCleanValue(o[f], !0), d && h <= i && h >= j) $(d.graphic).stop(!0, !0),
                d.graphic.animate({
                    angle: (l + (h - j) / p * c) / ka
                },
                {
                    duration: b
                }),
                g = m(n[f], e.dataLabels(h)),
                d.toolText = m(v[f], e.dataLabels(h)),
                q[f] == 0 && (g = ""),
                d.dataLabel && d.dataLabel.attr({
                    text: g
                })
            },
            render: function() {
                var b;
                var d, c = this.chart;
                d = c.renderer;
                var a = this.options;
                if (!this.group) b = this.group = d.g("series"),
                d = b,
                d.attr({
                    visibility: this.visible ? ba: HIDDEN,
                    zIndex: a.zIndex
                }).translate(c.plotLeft, c.plotTop).add(c.seriesGroup);
                this.drawGraph();
                this.drawTickMarks();
                this.drawPoints();
                this.drawTracker();
                this.drawDataLabels();
                if (this.visible) c.currentSeriesIndex = this.index,
                c.naviigator && placeNaviGator(c);
                this.options.animation && this.animate && this.animate();
                this.isDirty = !1
            },
            animate: function(d) {
                var c = this.data,
                a = this.options.gaugeStartAngle,
                b = 0,
                f, e = c && c.length;
                if (!d) {
                    for (; b < e; b += 1) f = c[b],
                    f.y && (d = f.graphic.attr("angle"), f.graphic.attr("angle", a / ka), f.graphic.animate({
                        angle: d
                    },
                    this.options.animation));
                    this.animate = null
                }
            }
        });
        N.angulargauge = Q
    }
})();