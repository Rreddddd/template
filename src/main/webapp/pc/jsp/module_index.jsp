<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" uri="http://www.0512.red/tags" %>
<t:templatePage id="pc-home">

    <t:templateContent id="head">
        <script>
            let table;
            home.on("init", function () {
                table = $("#table").table({
                    columns: [
                        [
                            {title: "序号", field: "order", align: "center", width: "5%"},
                            {title: "应用名称", field: "title", align: "left", width: "35%"},
                            {title: "应用链接", field: "url", align: "left", width: "40%"},
                            {
                                title: "操作", field: "operate", align: "center", width: "20%", formatter: function () {
                                    return '<button class="form-editor update">修改</button><button class="form-editor delete">删除</button>';
                                }
                            },
                        ]
                    ]
                });
                loadData();
            });

            function loadData() {
                $.ajax({
                    type: "get",
                    url: "/sys/module/data",
                    dataType: "json",
                    success: function (msg) {
                        table.table("loadData", msg);
                    }
                });
            }
        </script>
    </t:templateContent>
    <t:templateContent id="body">
        <div class="penal-header">
            <button class="form-editor add2">
                <i class="iconfont icon-add-s"></i>
                添加
            </button>
        </div>
        <div class="penal-body">
            <div class="custom-table" id="table">

            </div>
        </div>
    </t:templateContent>

</t:templatePage>