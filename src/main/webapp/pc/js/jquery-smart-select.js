/*
* 树型下拉选择
* create by Rred
* version code 1
* version name 1.0.0
*
* 1.0.0:增加拖动排序
*
* */
!function($,window,undefined){

    var jQTemplate={
        container : $('<div class="smart-select-container"></div>'),
        input : $('<div class="smart-select-input">' +
                    '<div class="select-input">' +
                        '<input type="text" readonly/>'+
                    '</div>'+
                    '<div class="presentation">' +
                        '<b class="presentationIcon"/>'+
                    '</div>'+
                '</div>'),
        panel : $('<div class="smart-select-panel">' +
                    '<div class="panel-title"><span/></div>'+
                    '<div class="panel-tree"></div>'+
                '</div>'),
        $window : $(window)
    };

    var Select=function(ele,option){
        var self=this;
        self.ele=ele.hide();
        self.id="smart-id-"+parseInt(Math.random()*100000);
        self.option=$.extend(true,{},self.defaultOption,option);
        self.jqElement={
            container : jQTemplate.container.clone().data("smartId",self.id),
            input : jQTemplate.input.clone().data("smartId",self.id),
            panel : jQTemplate.panel.clone().data("smartId",self.id)
        };
        self.jqElement._input=self.jqElement.input.find("input");
        if(self.option.multiple){
            self.ele.attr("multiple","multiple");
            if(self.option.selectParent===undefined){
                self.option.selectParent=true;
            }else{
                self.option.selectParent=self.option.selectParent===true;
            }
        }else{
            if(self.option.selectParent===undefined){
                self.option.selectParent=false;
            }else{
                self.option.selectParent=self.option.selectParent===true;
            }
        }
        self.loadSuccess=false;
        self.init();
    };

    Select.prototype={
        init : function(){
            var self=this;
            if(self.loadSuccess){
                self.destroy();
            }
            self.ids=[];
            self.texts=[];
            self.values=[];
            self.state={
                focused : false,
                open : false,
                disabled : self.option.disabled===true
            };
            self.ele.after(self.jqElement.container);
            if(self.option.type==="input"){
                self.jqElement.panel.addClass("_input");
                var inputHeight=Number(self.option.inputHeight);
                if(isNaN(inputHeight)){
                    inputHeight=31;
                }
                self.jqElement.input
                    .css("height",inputHeight)
                    .appendTo(self.jqElement.container)
                    .find(">.select-input")
                    .css("line-height",inputHeight-2+"px");
                self.jqElement._input.attr("placeholder",self.option.placeholder || "");
                self.jqElement.panel.appendTo($("body"));
            }else{
                self.jqElement.panel.addClass("_panel");
                self.jqElement.panel.appendTo(self.jqElement.container);
                self.jqElement.panel.find(">.panel-title>span").text(self.option.paneTitle);
            }
            if(self.option.sortable){
                self.jqElement.panel.addClass("sortable");
            }
            if(self.option.panelHeight!=="auto"){
                var panelHeight=Number(self.option.panelHeight);
                if(isNaN(panelHeight)){
                    panelHeight=200;
                }
                self.jqElement.panel.css("height",panelHeight);
            }
            if(self.option.panelWidth!=="auto"){
                var panelWidth=Number(self.option.panelWidth);
                if(isNaN(panelWidth)){
                    panelWidth=250;
                }
                self.jqElement.panel.css("min-width",panelWidth+"px");
            }
            self.jqElement.tree=new Tree(self.jqElement.panel.find(">.panel-tree"),self.option,self);
            self.bindEvent();
            self.loadSuccess=true;
            if(self.state.disabled){
                self.disable();
            }
            setTimeout(function(){
                self.option.onInit.apply(self);
                self.ele.triggerHandler("smart.select.init");
            },1);
        },
        bindEvent : function(){
            var self=this;
            if(self.option.type==="input"){
                self.jqElement.input.off("click").on("click",function(){
                    if(!self.state.disabled){
                        if(!self.state.focused){
                            self.jqElement.input.addClass("focused");
                            self.state.focused=true;
                        }
                        self.changePane();
                    }
                });
                jQTemplate.$window.off("click."+self.id).on("click."+self.id,function(event){
                    if(self.state.focused || self.state.open){
                        var $target=$(event.target);
                        var className=$target.attr("class") || "";
                        if(className.indexOf("smart-select-input")!==-1 || className.indexOf("smart-select-panel")!==-1){
                            if($target.data("smartId")!==self.id){
                                self.jqElement.input.removeClass("focused");
                                self.state.focused=false;
                                self.closePanel();
                            }
                        }else{
                            var element=$target.parents(".smart-select-input");
                            if(element.length===0){
                                element=$target.parents(".smart-select-panel");
                                if(element.length===0 || element.data("smartId")!==self.id){
                                    self.jqElement.input.removeClass("focused");
                                    self.state.focused=false;
                                    self.closePanel();
                                }
                            }else if(element.data("smartId")!==self.id){
                                self.jqElement.input.removeClass("focused");
                                self.state.focused=false;
                                self.closePanel();
                            }
                        }
                    }
                });
            }
        },
        changePane : function(){
            var self=this;
            if(self.state.open){
                self.closePanel();
            }else{
                self.openPanel();
            }
        },
        openPanel : function(){
            var self=this;
            if(!self.state.open){
                if(self.option.panelWidth==="auto"){
                    self.jqElement.panel.css("width",self.jqElement.input.width()+"px").show();
                }else{
                    self.jqElement.panel.css("width",Math.max(self.option.panelWidth,self.jqElement.input.width())+"px").show();
                }
                self.jqElement.panel.show();
                self.jqElement.input.addClass("open");
                self.state.open=true;
                self.resizePane();
            }
        },
        closePanel : function(){
            var self=this;
            if(self.state.open){
                self.jqElement.panel.hide();
                self.jqElement.input.removeClass("open");
                self.state.open=false;
            }
        },
        resizePane : function(){
            var self=this,left,top;
            var offset=self.jqElement.input.offset();
            let dif=jQTemplate.$window.width()-offset.left;
            let panelWidth=self.jqElement.panel.width();
            if(dif-panelWidth<20){
                let inputWidth=self.jqElement.input.width();
                if(inputWidth<panelWidth){
                    left=offset.left-(panelWidth-inputWidth);
                }else{
                    left=offset.left;
                }
            }else{
                left=offset.left;
            }
            dif=jQTemplate.$window.height()-(offset.top-jQTemplate.$window.scrollTop());
            let panelHeight=self.jqElement.panel.height();
            if(dif-panelHeight<20){
                top=offset.top-panelHeight;
            }else{
                top=offset.top+self.option.inputHeight;
            }
            self.jqElement.panel.css({
                left : left+"px",
                top : top+"px"
            });
        },
        destroy : function(){
            var self=this;
            self.ids=[];
            self.texts=[];
            self.values=[];
            self.jqElement._input.val("");
            self.jqElement.input.remove();
            self.jqElement.panel.remove();
            self.jqElement.container.remove();
            jQTemplate.$window.off("click."+self.id);
        },
        reLoad : function(){
            this.getTree().reLoad();
        },
        appendItem : function(row){
            this.getTree().appendItem(row);
        },
        appendChildItem : function(row){
            this.getTree().appendChildItem(row);
        },
        deleteItem : function(){
            this.getTree().deleteItem();
        },
        reBuildItem : function(row){
            this.getTree().reBuildItem(row);
        },
        loadData : function(data){
            this.getTree().loadData(data);
        },
        select : function(id,selectChildState,selectParentState,eventState){
            this.getTree().select(id,selectChildState,selectParentState,eventState);
        },
        selects : function(ids,selectChildState,selectParentState,eventState){
            this.getTree().selects(ids,selectChildState,selectParentState,eventState);
        },
        selectFirst : function(){
            this.getTree().selectFirst();
        },
        selectFirstChild : function(){
            this.getTree().selectFirstChild();
        },
        selectAll : function(){
            this.getTree().selectAll();
        },
        selectByType : function(value){
            this.getTree().selectByType(value);
        },
        unSelect : function(id,unSelectChildState,unSelectParentState,eventState){
            this.getTree().unSelect(id,unSelectChildState,unSelectParentState,eventState);
        },
        unSelects : function(ids,unSelectChildState,unSelectParentState,eventState){
            this.getTree().unSelects(ids,unSelectChildState,unSelectParentState,eventState);
        },
        clear : function(eventState){
            this.getTree().clear(eventState);
        },
        getId : function(){
            return this.ids[0] || "";
        },
        getIds : function(defaultAll,needParent){
            var self=this,ids;
            if(defaultAll && self.ids.length===0){
                ids=[];
                var children;
                eachTree(self.sourceRows,function(data){
                    children=data.children;
                    if(children!==undefined && children.length>0){
                        if(needParent && !data.readonly){
                            ids.push(data[self.option.idField]);
                        }
                    }else{
                        if(!data.readonly){
                            ids.push(data[self.option.idField]);
                        }
                    }
                });
            }else{
                if(needParent){
                    ids=self.ids;
                }else{
                    ids=[];
                    var values=self.values,value;
                    for(var i=0;i<values.length;i++){
                        value=values[i];
                        if(!value.children || value.children.length===0){
                            ids.push(value[self.option.idField]);
                        }
                    }
                }
            }
            return ids;
        },
        getText : function(separator){
            return this.texts.join(separator || ",");
        },
        getValue : function(){
            return this.values[0] || "";
        },
        getValues : function(defaultAll,needParent){
            var self=this,values;
            if(defaultAll && self.values.length===0){
                values=[];
                var children;
                eachTree(self.sourceRows,function(data){
                    children=data.children;
                    if(children!==undefined && children.length>0){
                        if(needParent && !data.readonly){
                            values.push(data);
                        }
                    }else{
                        if(!data.readonly){
                            values.push(data);
                        }
                    }
                });
            }else{
                if(needParent){
                    values=self.values;
                }else{
                    values=[];
                    var tempValues=self.values,value;
                    for(var i=0;i<tempValues.length;i++){
                        value=tempValues[i];
                        if(!value.children || value.children.length===0){
                            values.push(value);
                        }
                    }
                }
            }
            return values;
        },
        getData : function(){
            return getTreeData(this.jqElement.tree.jqElement.ul);
        },
        disable : function(){
            var self=this;
            self.jqElement.container.addClass("disabled");
            self.state.disabled=true;
        },
        enable : function(){
            var self=this;
            self.jqElement.container.removeClass("disabled");
            self.state.disabled=false;
        },
        getTree : function(){
            return this.jqElement.tree;
        },
        defaultOption : {
            type : "input", // input | panel
            idField : "id",
            textField : "text",
            inputHeight : 31,
            panelHeight : 200,//可以写 auto 表示自适应不出现滚动条
            panelWidth : "auto",//默认auto取父元素端口也可以指定固定宽度
            multiple : false,
            disabled : false,//是否禁用控件 可使用 disable/enable 方法手动 禁/启
            initLoadState : true,//装载完后是否请求url
            sortable : false,//是否开启拖动排序
            selectLinkage : true,//多选是否父子级联动
            selectParent : undefined,//指定是否可选父级 true/false 单选默认 false 多选默认 true
            checkable : true,
            placeholder : "",
            paneTitle : "项目部",
            data : undefined,//该参数不为空时，加载完成后将不发启url请求，reLoad方法不使用此参数。参数为空时加载开始会将option元素对应的值封装成此参数（无法封装成 tree 集合）
            async : true,//请求 异/同 步
            value : undefined, //配置url参数后有效，手动加载无效 值类型：单个id值 id值数组 固定字符串类型（“first”，“firstChild”，“all”）,手动加载可使用对应接口来达到相应效果
            url : undefined,
            params : { //url参数 object || function

            },
            onInit : function(){

            },
            onLoad : function(data){

            },
            onSelect : function(data){

            },
            onSelectAll : function(){

            },
            onUnSelect : function(data){

            },
            onUnSelectAll : function(){

            },
            onChange : function(){

            }
        }
    };

    var getTreeData=function(ul){
        if(!ul || ul.length===0){
            return [];
        }
        var rows=[],node,row,children;
        ul.find(">li").each(function(){
            node=$(this).find(">.smart-tree-node");
            row=node.data("rowData");
            children=getTreeData(node.next());
            if(children.length>0){
                row.children=children;
            }
            rows.push(row);
        });
        return rows;
    };

    var Loading=function(canvas,option){
        var self=this;
        var $canvas;
        if(canvas.length!==undefined){
            $canvas=canvas;
            canvas=$canvas[0];
        }else{
            $canvas=$(canvas);
        }
        $canvas.data("loadingInstance",self);
        self.intervalId=undefined;
        self.option=$.extend(true,{},self.defaultOption,option);
        self.option.ctx=canvas.getContext('2d');
        self.option.size = Math.min(canvas.clientWidth, canvas.clientHeight) || 60;
    };

    Loading.prototype={
        render : function(){
            var self=this;
            self.option.degreeStart = self.option.degreeStart + self.option.stepStart;
            self.option.degreeEnd = self.option.degreeEnd + self.option.stepEnd;
            if (self.option.degreeStart - 360 > self.option.degreeEnd) {
                self.option.degreeStart -= 720;
            }
            self.option.ctx.clearRect(0, 0, self.option.size, self.option.size);
            self.option.ctx.lineWidth = self.option.lineWidth;
            self.option.ctx.beginPath();
            self.option.ctx.strokeStyle = self.option.strokeStyle;
            self.option.ctx.arc(self.option.size / 2, self.option.size / 2, self.option.radius - self.option.lineWidth / 2, (self.option.degreeStart < self.option.degreeEnd ? self.option.degreeStart : self.option.degreeEnd) * Math.PI / 180, (self.option.degreeStart < self.option.degreeEnd ? self.option.degreeEnd : self.option.degreeStart) * Math.PI / 180, false);
            self.option.ctx.stroke();
        },
        play : function(){
            var self=this;
            self.intervalId=setInterval(function(){
                self.render();
            },10);
        },
        stop : function(){
            clearInterval(this.intervalId);
        },
        defaultOption : {
            radius : 30,
            lineWidth: 6,
            strokeStyle: '#4de2ff',
            degreeStart: -90,
            degreeEnd: 270,
            stepStart: 6,
            stepEnd: 3
        }
    };

    var Tree=function(container,option,selectInstance){
        var self=this;
        self.option=option;
        self.selectInstance=selectInstance;
        self.jqElement={
            container : container
        };
        self.sortOption={
            placeholder : $('<li class="node-placeholder"/>'),
            level : 0,
            scrollTop : 0,
            activeIndex : undefined,
            activeUl : undefined,
            activeLi : undefined,
            activeLis : undefined,
            offsetX : undefined,
            offsetY : undefined,
            beforeX : undefined,
            beforeY : undefined,
            expandNodes : undefined
        };
        self.jqElement.treeContainer=$('<div class="panel-tree-container"><ul class="panel-tree-wrapper"/></div>').appendTo(container);
        self.jqElement.ul=self.jqElement.treeContainer.find(">ul");
        self.jqElement.noData=$('<div class="panel-tree-holder">暂无数据</div>').appendTo(container);
        self.jqElement.mask=$('<div class="panel-tree-mask"><canvas width="60px" height="60px"/></div>').appendTo(container);
        self.jqElement.mask.data("loadingInstance",new Loading(self.jqElement.mask.find("canvas")));
        self.init();
    };

    var inflateRows=function(rows,_parentId,idField,selectParent){
        var row;
        for(var i=0;i<rows.length;i++){
            row=rows[i];
            $.extend(true,row,{_parentId:_parentId});
            if(row.children && row.children.length>0){
                if(selectParent!==true){
                    $.extend(true,row,{disabled:true});
                }
                inflateRows(row.children,row[idField],idField,selectParent)
            }
        }
    };

    var eachTree=function(data,handle){
        var children,row,result;
        for(var i=0;i<data.length;i++){
            row=data[i];
            result=handle(row);
            if(result===false){
                return false;
            }
            children=row.children;
            if(children===undefined || children.length===0){
                continue;
            }
            result=eachTree(children,handle);
            if(result===false){
                return false;
            }
        }
    };

    var getValidIds=function(rows,idField){
        var ids=[],row;
        for(var i=0;i<rows.length;i++){
            row=rows[i];
            if(row.disabled!==true && row.readonly!==true){
                ids.push(row[idField]);
            }
            if(row.children && row.children.length>0){
                var childIds=getValidIds(row.children,idField);
                for(var k=0;k<childIds.length;k++){
                    ids.push(childIds[k]);
                }
            }
        }
        return ids;
    };

    Tree.prototype={
        init : function(){
            var self=this;
            if(self.option.sortable){
                $(window.document).on({
                    "mouseup" : function(event){
                        if(event.button===0){
                            self.endSortable();
                        }
                    },
                    "mousemove" : function(event){
                        self.sorting(event);
                    }
                });
                self.jqElement.treeContainer.on("scroll",function(){
                    if(self.sortOption.activeLi){
                        var scrollTop=self.jqElement.treeContainer.scrollTop();
                        self.sortOption.offsetY+=scrollTop-self.sortOption.scrollTop;
                        self.sortOption.activeLi.css({
                            "top" : self.sortOption.offsetY+"px"
                        });
                        self.sortOption.scrollTop=scrollTop;
                    }
                });
            }
            self.jqElement.ul.on("mouseout",function(){
                self.jqElement.ul.find(".tree-node-focus").removeClass("tree-node-focus");
            });
            self.jqElement.noData.css("line-height",(self.option.panelHeight==="auto"?200:self.option.panelHeight)+"px");
            self.showNoData();
            if(self.option.data!==undefined && self.option.data.length>0){
                self.loadData(self.option.data);
                self.selectByType(self.option.value);
            }else if(self.option.initLoadState){
                self.reLoad();
            }
        },
        reLoad : function(){
            var self=this;
            if(self.option.url){
                self.showMask();
                var params=self.option.params;
                if($.isFunction(params)){
                    params=params.call();
                }
                $.ajax({
                    type : "get",
                    url : self.option.url,
                    data : params,
                    async : self.option.async,
                    dataType : "json",
                    success : function(msg){
                        self.loadData(msg);
                        self.selectByType(self.option.value);
                        self.hideMask();
                    },
                    error : function(){
                        self.hideMask();
                        self.showNoData();
                    }
                });
            }
        },
        loadData : function(data){
            var self=this,rows;
            if($.isArray(data)){
                rows=data;
            }else{
                rows=data.rows;
            }
            if(!rows || rows.length===0){
                self.showNoData();
                return;
            }
            self.hideNoData();
            inflateRows(rows,undefined,self.option.idField,self.option.selectParent);
            self.sourceRows=rows;
            self.validIds=getValidIds(rows,self.option.idField);
            self.clear(false);
            self.jqElement.ul.empty();
            self.selectInstance.ele.empty();
            for(var i=0;i<rows.length;i++){
                self.buildItem(self.jqElement.ul,rows[i],0);
            }
            setTimeout(function(){
                self.option.onLoad.apply(self);
                self.selectInstance.ele.triggerHandler("smart.select.load");
            },1);
        },
        appendItem : function(row){
            let self=this;
            let node = self.jqElement.treeContainer.find(".selected");
            if(node.length>0){
                self.buildItem(node.parent().parent(),row,node.data("level"));
                self.validIds.push(row[self.option.idField]);
                self.select(row[self.option.idField]);
            }
        },
        appendChildItem : function(row){
            let self=this;
            let node = self.jqElement.treeContainer.find(".selected");
            if(node.length>0){
                let ul = node.next();
                if(ul.length===0){
                    ul=$('<ul/>').appendTo(node.parent());
                }
                self.buildItem(ul,row,node.data("level")+1);
                self.validIds.push(row[self.option.idField]);
                self.select(row[self.option.idField]);
            }
        },
        deleteItem : function(){
            let self=this;
            let node = self.jqElement.treeContainer.find(".selected");
            if(node.length>0){
                let prev = node.parent().prev();
                if(prev.length===0){
                    prev=node.parent().parent().prev();
                }else{
                    prev=prev.find(">.smart-tree-node");
                }
                node.parent().remove();
                prev.click();
            }
        },
        reBuildItem : function(row){
            let self=this;
            let node = self.findItem(row[self.option.idField]);
            if(node.length===0){
                node=self.jqElement.treeContainer.find(".selected");
            }
            if(node.length>0){
                let rowData = node.data("rowData");
                $.extend(rowData,row);
                node.find(".tree-node-title").text(rowData[self.option.textField] || "");
                node.find(".tree-node-icon").attr("class",'tree-node-icon '+(row.iconClass || "")).css("color",row.iconColor || "");
            }
        },
        buildItem : function(container,row,level){
            var self=this,i;
            var li=$('<li>' +
                        '<div class="smart-tree-node">' +
                        '</div>'+
                    '</li>').appendTo(container);
            var node=li.find(".smart-tree-node").attr("data-id",row[self.option.idField]).data("level",level).data("rowData",row);
            for(i=0;i<level;i++){
                node.append('<span class="tree-node-indent"/>');
            }
            if(row.children && row.children.length>0){
                node.append($('<span class="tree-node-hit tree-node-collapsed"/>').on({
                    "mouseover" : function(){
                        var _this=$(this);
                        if(_this.hasClass("tree-node-collapsed")){
                            _this.addClass("tree-node-collapsed-hover");
                        }else{
                            _this.addClass("tree-node-expanded-hover");
                        }
                    },
                    "mouseout" : function(){
                        var _this=$(this);
                        if(_this.hasClass("tree-node-collapsed")){
                            _this.removeClass("tree-node-collapsed-hover");
                        }else{
                            _this.removeClass("tree-node-expanded-hover");
                        }
                    },
                    "click" : function(event){
                        event.stopPropagation();
                        self.toggle($(this).parent());
                    }
                }));
                var childrenContainer=$('<ul style="display: none;"/>').appendTo(li);
                for(i=0;i<row.children.length;i++){
                    self.buildItem(childrenContainer,row.children[i],level+1);
                }
                if(row.state!=="closed"){
                    node.find(">.tree-node-hit").click();
                }
                node.append('<span class="tree-node-icon '+(row.iconClass || "tree-node-folder")+'"'+(row.iconColor?' style="color:'+row.iconColor+'"':'')+'/>');
            }else{
                node.append('<span class="tree-node-indent"/>');
                node.append('<span class="tree-node-icon '+(row.iconClass || "tree-node-file")+'"'+(row.iconColor?' style="color:'+row.iconColor+'"':'')+'/>');
            }
            if(row.disabled===true){
                node.addClass("_disabled").data("disabled",true);
            }else if(self.option.checkable){
                node.append('<span class="tree-node-checkbox tree-node-checkbox0"/>');
            }else{
                node.addClass("selectable");
            }
            var text=row[self.option.textField] || "";
            node.append($('<span class="tree-node-title"/>').text(text));
            node.data("readonly",row.readonly===true);
            if(row.readonly===true && node.data("disabled")!==true){
                node.addClass("_readonly").find(".tree-node-checkbox").attr("class","tree-node-checkbox tree-node-checkbox3");
            }
            node.on("mouseover",function(){
                self.jqElement.ul.find(".tree-node-focus").removeClass("tree-node-focus");
                if(!node.data("readonly") && !node.data("disabled")){
                    node.addClass("tree-node-focus");
                }
            }).on("click",function(){
                if(!node.data("readonly") && !node.data("disabled")){
                    if(self.contains(row[self.option.idField])){
                        if(self.option.multiple){
                            self.unSelect(node,self.option.selectLinkage,self.option.selectLinkage,true);
                        }
                    }else{
                        self.select(node,self.option.selectLinkage,self.option.selectLinkage,true);
                    }
                }
            });
            self.selectInstance.ele.append($('<option value="'+node.data("id")+'" data-readonly="'+node.data("readonly")+'"/>').text(text));
            if(self.option.sortable){
                node.find(".tree-node-icon").on({
                    "click" : function(event){
                        event.stopPropagation();
                    },
                    "mousedown" : function(event){
                        if(event.button===0){
                            self.startSortable(li);
                        }
                    }
                });
            }
        },
        startSortable : function(li){
            var self=this;
            self.sortOption.activeLi=li;
            self.sortOption.activeUl=li.parent();
            self.sortOption.level=li.find(">.smart-tree-node").find(".tree-node-indent").length+li.find(">.smart-tree-node").find(".tree-node-hit").length;
            self.sortOption.activeLis=self.sortOption.activeUl.addClass("sorting").find(">li");
            self.sortOption.expandNodes=[];
            self.sortOption.activeLis.each(function(i){
                var _this=$(this);
                _this.data("sortIndex",i);
                var node=_this.find(">.smart-tree-node");
                if(self.isExpand(node)){
                    self.collapse(node);
                    self.sortOption.expandNodes.push(node);
                }
            });
            li.after(self.sortOption.placeholder);
            var liWidth=li.width();
            self.sortOption.activeIndex=li.data("sortIndex");
            self.sortOption.offsetX=0;
            self.sortOption.offsetY=self.sortOption.activeIndex*20;
            self.sortOption.scrollTop=self.jqElement.treeContainer.scrollTop();
            li.css({
                "position" : "absolute",
                "min-width" : liWidth+"px",
                "opacity" : "0.5",
                "top" : self.sortOption.offsetY+"px",
                "left" : self.sortOption.offsetX+"px"
            });
        },
        sorting : function(event){
            var self=this;
            if(self.sortOption.activeLi){
                if(self.sortOption.beforeX===undefined){
                    self.sortOption.beforeX=event.pageX;
                    self.sortOption.beforeY=event.pageY;
                }
                self.sortOption.offsetX+=event.pageX-self.sortOption.beforeX;
                self.sortOption.offsetY+=event.pageY-self.sortOption.beforeY;
                self.sortOption.activeLi.css({
                    "left" : self.sortOption.offsetX+"px",
                    "top" : self.sortOption.offsetY+"px"
                });
                self.sortOption.beforeX=event.pageX;
                self.sortOption.beforeY=event.pageY;
                //----------------------------------------------
                var index=parseInt(self.sortOption.offsetY/20);
                index=index<0?0:index;
                var count=self.sortOption.activeLis.length;
                index=index>=count?count-1:index;
                if(index!==self.sortOption.activeIndex || index===0){
                    self.sortOption.placeholder.remove();
                    if(self.sortOption.offsetY<0){
                        self.sortOption.activeLis.eq(index).before(self.sortOption.placeholder);
                    }else{
                        self.sortOption.activeLis.eq(index).after(self.sortOption.placeholder);
                    }
                    self.sortOption.activeIndex=index;
                }
            }
        },
        endSortable : function(){
            var self=this;
            if(self.sortOption.activeLi){
                self.sortOption.activeUl.removeClass("sorting");
                self.sortOption.activeLi.removeAttr("style");
                self.sortOption.placeholder.after(self.sortOption.activeLi.detach() );
                self.sortOption.placeholder.remove();
                self.sortOption.activeLi=undefined;
                self.sortOption.beforeX=undefined;
                self.sortOption.beforeY=undefined;
                for(var i=0;i<self.sortOption.expandNodes.length;i++){
                    self.expand(self.sortOption.expandNodes[i]);
                }
            }
        },
        findItem : function(id){
            return this.jqElement.ul.find(".smart-tree-node[data-id='"+id+"']");
        },
        isExpand : function(id){
            var self=this,node;
            if(typeof id!=="object"){
                node=self.findItem(id);
            }else{
                node=id;
            }
            var hit=node.find(".tree-node-hit");
            return hit.length>0 && hit.hasClass("tree-node-expanded");
        },
        toggle : function(id){
            var self=this;
            if(self.isExpand(id)){
                self.collapse(id);
            }else{
                self.expand(id);
            }
        },
        expand : function(id){
            var self=this,node;
            if(typeof id!=="object"){
                node=self.findItem(id);
            }else{
                node=id;
            }
            var hit=node.find(".tree-node-hit");
            if(hit.length>0){
                if(!hit.hasClass("tree-node-expanded")){
                    hit.attr("class","tree-node-hit tree-node-expanded"+(hit.hasClass("tree-node-collapsed-hover")?" tree-node-expanded-hover":""));
                    node.next().show();
                }
            }
        },
        collapse : function(id){
            var self=this,node;
            if(typeof id!=="object"){
                node=self.findItem(id);
            }else{
                node=id;
            }
            var hit=node.find(".tree-node-hit");
            if(hit.length>0){
                if(!hit.hasClass("tree-node-collapsed")){
                    hit.attr("class","tree-node-hit tree-node-collapsed"+(hit.hasClass("tree-node-expanded-hover")?" tree-node-collapsed-hover":""));
                    node.next().hide();
                }
            }
        },
        contains : function(id){
            var ids=this.selectInstance.ids;
            for(var i=0;i<ids.length;i++){
                if(ids[i]===id){
                    return true;
                }
            }
            return false;
        },
        checkValidId : function(id){
            var ids=this.validIds;
            for(var i=0;i<ids.length;i++){
                if(ids[i]===id){
                    return true;
                }
            }
            return false;
        },
        select : function(id,selectChildState,selectParentState,eventState){
            var self=this,node;
            if(typeof id==="object"){
                node=id;
            }else{
                node=self.findItem(id);
            }
            if(node.length===0){
                return;
            }
            var value=node.data("rowData");
            id=value[self.option.idField];
            if(!self.checkValidId(id) || self.contains(id)){
                return;
            }
            if(!self.option.multiple){
                self.clear(false);
                self.selectInstance.closePanel();
            }
            eventState=eventState===undefined?true:eventState;
            selectChildState=selectChildState===undefined?self.option.selectLinkage:selectChildState;
            selectParentState=selectParentState===undefined?self.option.selectLinkage:selectParentState;
            self.selectInstance.ids.push(id);
            self.selectInstance.texts.push(value[self.option.textField]);
            self.selectInstance.values.push(value);
            if(node.hasClass("selectable")){
                node.addClass("selected");
            }
            node.find(".tree-node-checkbox").attr("class","tree-node-checkbox tree-node-checkbox1");
            self.selectInstance.jqElement._input.val(self.selectInstance.texts.join(","));
            if(self.option.multiple && self.option.selectParent){
                self.selectInstance.ele.val(self.selectInstance.ids);
                if(selectChildState && value.children && value.children.length>0){//级联子集
                    self.selects(value.children,true,false,false);
                }
                if(selectParentState && value._parentId){//级联父集
                    var parentNode=self.findItem(value._parentId);
                    if(parentNode.length>0){
                        var parentData=parentNode.data("rowData"),state=true;
                        for(var i=0;i<parentData.children.length;i++){
                            if(!self.contains(parentData.children[i][self.option.idField])){
                                state=false;
                                break;
                            }
                        }
                        if(state){
                            self.select(parentData[self.option.idField],false,true,false);
                        }else{
                            while(parentNode.length>0){
                                parentNode.find(".tree-node-checkbox").attr("class","tree-node-checkbox tree-node-checkbox2");
                                if(!parentData._parentId){
                                    break;
                                }
                                parentNode=self.findItem(parentData._parentId);
                                parentData=parentNode.data("rowData");
                            }
                        }
                    }
                }
            }else{
                self.selectInstance.ele.val(self.selectInstance.ids[0] || "");
            }
            if(eventState){
                self.option.onSelect.apply(self,[value]);
                self.option.onChange.apply(self);
                self.selectInstance.ele.triggerHandler("smart.select.select",[value]);
                self.selectInstance.ele.triggerHandler("smart.select.change");
            }
        },
        selects : function(ids,selectChildState,selectParentState,eventState){
            if(ids!==undefined && $.isArray(ids)){
                var self=this,id;
                if(self.option.multiple){
                    for(var i=0;i<ids.length;i++){
                        id=ids[i];
                        if(typeof id==="object"){
                            id=id[self.option.idField];
                        }
                        self.select(id,selectChildState,selectParentState,eventState);
                    }
                }else{
                    self.select(ids[0],selectChildState,selectParentState,eventState);
                }
            }
        },
        selectFirst : function(){
            var self=this;
            if(self.sourceRows){
                if(self.option.multiple){
                    eachTree(self.sourceRows,function(row){
                        if(row.readonly!==true){
                            self.select(row[self.option.idField],true,true,true);
                            return false;
                        }
                    });
                }else{
                    self.selectFirstChild();
                }
            }
        },
        selectFirstChild : function(){
            var self=this;
            if(self.sourceRows){
                eachTree(self.sourceRows,function(data){
                    if(data.readonly!==true && (data.children===undefined || data.children.length===0)){
                        self.select(data[self.option.idField],true,true,true);
                        return false;
                    }
                });
            }
        },
        selectAll : function(){
            var self=this;
            if(self.sourceRows){
                eachTree(self.sourceRows,function(data){
                    if(data.readonly!==true){
                        self.select(data[self.option.idField],false,false,false);
                    }
                });
                self.option.onSelectAll.apply(self);
                self.option.onChange.apply(self);
                self.selectInstance.ele.triggerHandler("smart.select.selectAll");
                self.selectInstance.ele.triggerHandler("smart.select.change");
            }
        },
        selectByType : function(value){
            if(value!==undefined){
                var self=this;
                if($.isArray(value)){
                    self.selects(value,true,true,true);
                }else if(value==="first"){
                    self.selectFirst();
                }else if(value==="firstChild"){
                    self.selectFirstChild();
                }else if(value==="all"){
                    if(self.option.multiple){
                        self.selectAll();
                    }else{
                        self.selectFirstChild();
                    }
                }else{
                    self.select(value,true,true,true);
                }
            }
        },
        unSelect : function(id,unSelectChildState,unSelectParentState,eventState){
            var self=this,node;
            if(typeof id==="object"){
                node=id;
            }else{
                node=self.findItem(id);
            }
            if(node.length===0){
                return;
            }
            var value=node.data("rowData");
            id=value[self.option.idField];
            if(!self.checkValidId(id)){
                return;
            }
            eventState=eventState===undefined?true:eventState;
            unSelectChildState=unSelectChildState===undefined?true:unSelectChildState;
            unSelectParentState=unSelectParentState===undefined?true:unSelectParentState;
            if(self.contains(id)){
                var values=self.selectInstance.values,tempValue;
                self.selectInstance.ids=[];
                self.selectInstance.texts=[];
                self.selectInstance.values=[];
                for(var i=0;i<values.length;i++){
                    tempValue=values[i];
                    if(tempValue[self.option.idField]===id){
                        continue;
                    }
                    self.selectInstance.ids.push(tempValue[self.option.idField]);
                    self.selectInstance.texts.push(tempValue[self.option.textField]);
                    self.selectInstance.values.push(tempValue);
                }
                self.selectInstance.jqElement._input.val(self.selectInstance.texts.join(","));
            }
            if(!unSelectChildState && unSelectParentState && value.children && value.children.length>0){
                var state=false;
                for(i=0;i<value.children.length;i++){
                    if(self.contains(value.children[i][self.option.idField]) || self.findItem(value.children[i][self.option.idField]).find(".tree-node-checkbox").hasClass("tree-node-checkbox2")){
                        state=true;
                        break;
                    }
                }
                if(state){
                    node.find(".tree-node-checkbox").attr("class","tree-node-checkbox tree-node-checkbox2");
                }else{
                    node.find(".tree-node-checkbox").attr("class","tree-node-checkbox tree-node-checkbox0");
                }
            }else{
                node.find(".tree-node-checkbox").attr("class","tree-node-checkbox tree-node-checkbox0");
            }
            if(node.hasClass("selectable")){
                node.removeClass("selected");
            }
            if(self.option.multiple && self.option.selectParent){
                self.selectInstance.ele.val(self.selectInstance.ids);
                if(unSelectChildState && value.children && value.children.length>0){//级联子集
                    self.unSelects(value.children,true,false,false);
                }
                if(unSelectParentState && value._parentId){//级联父集
                    self.unSelect(value._parentId,false,true,false);
                }
            }else{
                self.selectInstance.ele.val(self.selectInstance.ids[0] || "");
            }
            if(eventState){
                self.option.onUnSelect.apply(self,[value]);
                self.option.onChange.apply(self);
                self.selectInstance.ele.triggerHandler("smart.select.unSelect",[value]);
                self.selectInstance.ele.triggerHandler("smart.select.change");
            }
        },
        unSelects : function(ids,unSelectChildState,unSelectParentState,eventState){
            if(ids!==undefined && $.isArray(ids)){
                var self=this,id;
                for(var i=0;i<ids.length;i++){
                    id=ids[i];
                    if(typeof id==="object"){
                        id=id[self.option.idField];
                    }
                    self.unSelect(id,unSelectChildState,unSelectParentState,eventState);
                }
            }
        },
        clear : function(eventState){
            eventState=eventState===undefined?true:eventState;
            var self=this;
            var ids=self.selectInstance.ids;
            for(var i=0;i<ids.length;i++){
                self.findItem(ids[i]).find(".tree-node-checkbox").attr("class","tree-node-checkbox tree-node-checkbox0");
            }
            self.selectInstance.ids=[];
            self.selectInstance.texts=[];
            self.selectInstance.values=[];
            self.selectInstance.jqElement._input.val("");
            if(self.option.multiple){
                self.selectInstance.ele.val([]);
            }else{
                self.selectInstance.ele.val("");
            }
            self.jqElement.container.find(".smart-tree-node.selectable").removeClass("selected");
            if(eventState){
                self.option.onUnSelectAll.apply(self);
                self.option.onChange.apply(self);
                self.selectInstance.ele.triggerHandler("smart.select.unSelectAll");
                self.selectInstance.ele.triggerHandler("smart.select.change");
            }
        },
        showNoData : function(){
            this.jqElement.noData.show();
            this.jqElement.treeContainer.hide();
        },
        hideNoData : function(){
            this.jqElement.noData.hide();
            this.jqElement.treeContainer.show();
        },
        showMask : function(){
            this.jqElement.mask.data("loadingInstance").play();
            this.jqElement.mask.show();
        },
        hideMask : function(){
            this.jqElement.mask.data("loadingInstance").stop();
            this.jqElement.mask.hide();
        }
    };

    $.fn.extend({
        smartSelect : function(){
            if(this[0].tagName!=="SELECT"){
                console.error("smartSelect attach oneself to <select/> dom");
            }else{
                var temp=arguments[0];
                var instance=this.data("smartSelectInstance");
                if(typeof temp==='string'){
                    if(instance!==undefined){
                        var fun=instance[temp];
                        if($.isFunction(fun)){
                            var params=Arrays.clone(arguments);
                            params.shift();
                            var result=fun.apply(instance,params);
                            if(result!==undefined){
                                return result;
                            }
                            return this;
                        }
                    }
                }else if(temp===undefined || typeof temp==='object'){
                    if(instance===undefined){
                        var rows=[],option;
                        this.find(">option").each(function(){
                            option=$(this);
                            rows.push({
                                id : option.attr("value"),
                                text : option.text(),
                                readonly : option.data("readonly")==="true"
                            });
                        });
                        if(temp===undefined){
                            temp={};
                        }
                        if(!temp.data){
                            temp.data=rows;
                        }
                        if(temp.disabled===undefined){
                            temp.disabled=this.attr("disabled")==="disabled";
                        }
                        instance=new Select(this,temp);
                        this.data("smartSelectInstance",instance);
                    }else{
                        $.extend(true,instance.option,temp);
                        instance.init();
                    }
                    return instance;
                }
            }
        }
    });
}(jQuery,window,undefined);