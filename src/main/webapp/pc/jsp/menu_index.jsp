<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" uri="http://www.0512.red/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:templatePage id="pc-home">

    <t:templateContent id="head">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/pc/css/jquery.tagsinput-revisited.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/pc/css/jquery-smart-select.css">
        <script src="${pageContext.request.contextPath}/pc/js/jquery.tagsinput-revisited.js"></script>
        <script src="${pageContext.request.contextPath}/pc/js/jquery-smart-select.js"></script>
        <script src="${pageContext.request.contextPath}/pc/js/farbtastic-1.3.js"></script>
        <style type="text/css">
            .smart-select-container {
                height: 95%;
            }

            .menu-wrapper {
                display: flex;
                height: 79%;
            }

            .menu-tree {
                flex: 0 0 300px;
            }

            .menu-info {
                flex: 1 1 100%;
                padding-left: 50px;
            }

            .menu-info-wrapper {
                max-width: 400px;
            }

            .menu-save-btn {
                padding-top: 50px;
            }

            .menu-save-btn > button {
                width: 100%;
            }

            .tagsinput {
                line-height: initial;
            }

            .pick-icon {
                position: absolute;
                right: 2px;
                color: blue;
                cursor: pointer;
            }

            .color-picker {
                display: none;
                position: absolute;
                right: -240px;
                top: -88px;
            }
        </style>
        <script>
            let menuTree;
            home.on("init", function () {
                let menuInfo = $(".menu-info-wrapper");
                let param_title = menuInfo.find("input[name='title']");
                let param_icon_class = menuInfo.find("select[name='iconClass']");
                param_icon_class.smartSelect({
                    idField: "id",
                    textField: "value",
                    url: "/sys/menu/getIconfont"
                });
                let param_icon_color = menuInfo.find("input[name='iconColor']");
                let param_auths = menuInfo.find("input[name='auths']");
                let param_module = menuInfo.find("select[name='module']");
                menuTree = $("#menu-tree");
                let beforeSelectId;
                menuTree.smartSelect({
                    idField: "id",
                    textField: "title",
                    panelHeight: "auto",
                    type: "panel",
                    paneTitle: "菜单列表",
                    sortable: true,
                    selectParent: true,
                    checkable: false,
                    onSelect: function (rowData) {
                        if (beforeSelectId) {
                            updateTreeItem(beforeSelectId);
                        }
                        beforeSelectId = rowData.id;
                        param_title.val(rowData.title);
                        param_icon_class.smartSelect("select", rowData.iconClass || "-1");
                        param_icon_color.val(rowData.iconColor);
                        $.farbtastic("#color-picker").setColor(rowData.iconColor || "#000000");
                        param_auths.val("").clearTag();
                        let permissions = rowData.permissions;
                        if (permissions && permissions.length > 0) {
                            for (let i = 0; i < permissions.length; i++) {
                                param_auths.addTag({
                                    id: permissions[i].authId + "_" + permissions[i].type,
                                    value: permissions[i].authName
                                }, {
                                    focus: true,
                                    color: permissions[i].type ? "#0079ff" : "#556270"
                                });
                            }
                        }
                        if (rowData.module) {
                            param_module.val(rowData.module.id + "");
                        } else {
                            param_module.val("-1");
                        }
                        disableSelectedModules();
                    }
                });
                loadTree();
                param_auths.tagsInput({
                    placeholder: '添加成员',
                    width: '100%',
                    minHeight: '140px',
                    defaultText: "添加授权",
                    tagColor: function (rowData) {
                        return rowData.id.split("_")[1] === "0" ? "#0079ff" : "#556270";
                    },
                    autocomplete: {
                        idField: "id",
                        textField: "value",
                        unitField: "unit",
                        source: '/sys/menu/searchAuths',
                        minLength: 0
                    }
                });
                let pickIcon = $("#pick-icon").on("click", function () {
                    let state = (pickIcon.data("state") + "") === "true";
                    if (state) {
                        colorPicker.hide();
                    } else {
                        colorPicker.show();
                    }
                    pickIcon.data("state", !state);
                });
                $("#append").on("click", function () {
                    menuTree.smartSelect("appendItem", {
                        id: -1 * Math.ceil(Math.random() * 1000000),
                        title: "未命名"
                    });
                });
                $("#appendChild").on("click", function () {
                    menuTree.smartSelect("appendChildItem", {
                        id: -1 * Math.ceil(Math.random() * 1000000),
                        title: "未命名"
                    });
                });
                $("#delete").on("click", function () {
                    beforeSelectId=undefined;
                    menuTree.smartSelect("deleteItem");
                });
                let colorPicker = $("#color-picker");
                colorPicker.farbtastic(function (color) {
                    param_icon_color.val(color);
                }, 200);
                let saveState = false;
                $("#save-btn").on("click", function () {
                    if (saveState) {
                        return;
                    }
                    saveState = true;
                    updateTreeItem(beforeSelectId);
                    let saveRows = [], savePermissions = [];
                    formatSaveRows(menuTree.smartSelect("getData"), saveRows, savePermissions, -1);
                    $.ajax({
                        type: "post",
                        url: "/sys/menu/save",
                        data: JSON.stringify({
                            menus: saveRows,
                            permissions: savePermissions,
                        }),
                        dataType: "json",
                        contentType: "application/json",
                        success: function (msg) {
                            saveState = false;
                            if (msg.errorCode === 0) {
                                $.alert("保存成功");
                            } else {
                                $.alert("保存失败");
                            }
                        }
                    });
                });

                function updateTreeItem(id) {
                    let values = param_auths.data("values");
                    let permissions = [];
                    if (values && values.length > 0) {
                        let id;
                        for (let i = 0; i < values.length; i++) {
                            id = values[i].id;
                            let id_type = id.split("_");
                            permissions.push({
                                authId: id_type[0],
                                type: id_type[1],
                                authName: values[i].value
                            });
                        }
                    }
                    menuTree.smartSelect("reBuildItem", {
                        id: id,
                        title: param_title.val(),
                        iconClass: param_icon_class.val(),
                        iconColor: param_icon_color.val(),
                        permissions: permissions,
                        module: {
                            id: param_module.val()
                        }
                    });
                }

                function disableSelectedModules() {
                    param_module.find("option").removeAttr("disabled");
                    _disableSelectedModules(menuTree.smartSelect("getData"));
                }

                function _disableSelectedModules(rows) {
                    if (rows && rows.length > 0) {
                        let rowData;
                        for (let i = 0; i < rows.length; i++) {
                            rowData = rows[i];
                            if (rowData.id!==beforeSelectId && rowData.module) {
                                param_module.find(".id_" + rowData.module.id).attr("disabled", true);
                            }
                            _disableSelectedModules(rowData.children);
                        }
                    }
                }
            });

            function formatSaveRows(rows, saveRows, savePermissions, parentId) {
                if (rows && rows.length > 0) {
                    let rowData, saveRowData, permissions;
                    for (let i = 0; i < rows.length; i++) {
                        rowData = rows[i];
                        saveRowData = {
                            id: saveRows.length + 1,
                            parentId: parentId,
                            title: rowData.title,
                            iconClass: rowData.iconClass,
                            iconColor: rowData.iconColor,
                            module: rowData.module,
                            order: i + 1
                        };
                        saveRows.push(saveRowData);
                        permissions = rowData.permissions;
                        if (permissions && permissions.length > 0) {
                            for (let k = 0; k < permissions.length; k++) {
                                savePermissions.push({
                                    id: savePermissions.length + 1,
                                    authId: permissions[k].authId,
                                    type: permissions[k].type,
                                    menuId: saveRowData.id
                                });
                            }
                        }
                        formatSaveRows(rowData.children, saveRows, savePermissions, saveRowData.id);
                    }
                }
            }

            function loadTree() {
                $.ajax({
                    type: "get",
                    url: "/sys/menu/data",
                    dataType: "json",
                    success: function (msg) {
                        menuTree.smartSelect("loadData", formatTreeData(msg));
                        menuTree.smartSelect("selectFirst");
                    }
                });
            }

            function formatTreeData(rows) {
                let formatRows = [], rowData;
                for (let i = 0; i < rows.length; i++) {
                    rowData = rows[i];
                    formatRows.push($.extend(rowData.menu, {
                        children: formatTreeData(rowData.children),
                        permissions: rowData.permissions
                    }));
                }
                return formatRows;
            }
        </script>
    </t:templateContent>
    <t:templateContent id="body">
        <div class="penal-header">
            <button class="form-editor add2" id="append">
                <i class="iconfont icon-add-s"></i>
                添加
            </button>
            <button class="form-editor add2" id="appendChild">
                <i class="iconfont icon-add-s"></i>
                添加下级
            </button>
            <button class="form-editor delete" id="delete">
                <i class="iconfont icon-shanchu"></i>
                删除
            </button>
        </div>
        <div class="penal-body menu-wrapper">
            <div class="menu-tree">
                <select id="menu-tree"></select>
            </div>
            <div class="menu-info">
                <div class="menu-info-wrapper">
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">菜单名称 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="20" type="text" name="title" id="param-title"
                                       placeholder="输入菜单名称(必填)">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">应用图标 :</span>
                            <div class="edit-value">
                                <select class="edit-control" name="iconClass" id="param-iconClass"></select>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">应用颜色 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="20" type="text" name="iconColor"
                                       id="param-iconColor"
                                       placeholder="输入应用颜色(16进制带#号)">
                                <span id="pick-icon" class="pick-icon" data-state="false">选取</span>
                                <div id="color-picker" class="color-picker"></div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">应用授权 :</span>
                            <div class="edit-value">
                                <input class="edit-control" id="auths" name="auths"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">选择应用 :</span>
                            <div class="edit-value">
                                <select class="edit-control" name="module" id="param-module">
                                    <option value="-1">--选择应用--</option>
                                    <c:forEach var="module" items="${modules}">
                                        <option value="${module.id}" class="id_${module.id}">${module.title}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-row menu-save-btn">
                        <button class="form-editor info" id="save-btn">保存</button>
                    </div>
                </div>
            </div>
        </div>
    </t:templateContent>
</t:templatePage>