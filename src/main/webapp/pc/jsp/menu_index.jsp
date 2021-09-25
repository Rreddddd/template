<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" uri="http://www.0512.red/tags" %>
<t:templatePage id="pc-home">

    <t:templateContent id="head">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/pc/css/jquery-smart-select.css">
        <script src="${pageContext.request.contextPath}/pc/js/jquery-smart-select.js"></script>
        <style type="text/css">
            .smart-select-container{
                height: 95%;
            }
            .menu-wrapper{
                display: flex;
                height: 79%;
            }
            .menu-tree{
                flex: 0 0 300px;
            }
            .menu-info{
                flex: 1 1 100%;
            }
        </style>
        <script>
            home.on("init",function(){
                $("#menu-tree").smartSelect({
                    idField : "id",
                    textField : "text",
                    panelHeight : "auto",
                    url: "/sys/menu/data",
                    type : "panel",
                    paneTitle : "菜单列表",
                    sortable : true
                });
            });
        </script>
    </t:templateContent>
    <t:templateContent id="body">
        <div class="penal-header">
            <button class="form-editor add2">
                <i class="iconfont icon-add-s"></i>
                添加
            </button>
        </div>
        <div class="penal-body menu-wrapper">
            <div class="menu-tree">
                <select id="menu-tree"></select>
            </div>
            <div class="menu-info">

            </div>
        </div>
    </t:templateContent>

</t:templatePage>