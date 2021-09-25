<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" uri="http://www.0512.red/tags" %>
<t:templatePage id="pc-home">

    <t:templateContent id="head">
        <script>
            let table, addModal;
            home.on("init", function () {
                table = $("#table").table({
                    page: true,
                    url: "/sys/module/data",
                    columns: [
                        [
                            {title: "序号", field: "order", align: "center", width: "5%"},
                            {title: "应用名称", field: "title", align: "left", width: "35%"},
                            {title: "应用链接", field: "url", align: "left", width: "40%"},
                            {
                                title: "操作",
                                field: "operate",
                                align: "center",
                                width: "20%",
                                formatter: function (index, field, rowData) {
                                    let operate = $('<button class="form-editor update">修改</button><button class="form-editor delete">删除</button>');
                                    operate.eq(0).on("click", function () {
                                        edit(rowData);
                                    });
                                    operate.eq(1).on("click", function () {
                                        remove(rowData);
                                    });
                                    return operate;
                                }
                            },
                        ]
                    ]
                });
                $("#add").on("click", add);
                addModal = $("#module-add-modal").modal({
                    initOpen: false,
                    onConfirm: function () {
                        let id = addModal.data("id");
                        let title = addModal.find("input[name='title']").val();
                        if (!title) {
                            $.alert("模块名称不能为空");
                            return;
                        }
                        let url = addModal.find("input[name='url']").val();
                        if (!url) {
                            $.alert("模块名称不能为空");
                            return;
                        }
                        let module = {
                            id: id,
                            title: title,
                            url: url
                        };
                        $.ajax({
                            type: "post",
                            url: "/sys/module/" + (module.id ? "update" : "add"),
                            data: JSON.stringify(module),
                            dataType: "json",
                            contentType: "application/json",
                            success: function (msg) {
                                if (msg.errorCode === 0) {
                                    addModal.modal("close");
                                    $.alert("修改成功");
                                    table.table("requestData");
                                } else {
                                    $.alert("修改失败");
                                }
                            }
                        });
                    }
                });
            });

            function add() {
                addModal.find("input[name='title']").val("");
                addModal.find("input[name='url']").val("");
                addModal.data("id", "").modal("open");
            }

            function edit(rowData) {
                addModal.find("input[name='title']").val(rowData.title);
                addModal.find("input[name='url']").val(rowData.url);
                addModal.data("id", rowData.id).modal("open");
            }

            function remove(rowData) {
                $.alert({
                    content: "确定删除?",
                    btn: $.alert.btn.yesAndNo,
                    onClickBtn: function (type) {
                        if (type === $.alert.btn.yes) {
                            $.ajax({
                                type: "post",
                                url: "/sys/module/delete",
                                data: {
                                    id: rowData.id
                                },
                                dataType: "json",
                                success: function (msg) {
                                    if (msg.errorCode === 0) {
                                        $.alert("删除成功");
                                        table.table("requestData");
                                    } else {
                                        $.alert("删除失败");
                                    }
                                }
                            });
                        }
                        this.close();
                    }
                });
            }
        </script>
    </t:templateContent>
    <t:templateContent id="body">
        <div class="penal-header">
            <button class="form-editor add2" id="add">
                <i class="iconfont icon-add-s"></i>
                添加
            </button>
        </div>
        <div class="penal-body">
            <div class="custom-table" id="table">

            </div>
        </div>
        <div class="modal-container" id="module-add-modal">
            <div class="modal-header">
                <span class="modal-icon "></span>
                <span class="modal-title">添加新模块</span>
                <div class="modal-close">x</div>
            </div>
            <div class="modal-body">
                <div class="user-info-container">
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">应用名称 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="20" type="text" name="title"
                                       placeholder="输入应用名称(必填)">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <label class="edit-wrapper">
                            <span class="edit-title">应用链接 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="50" type="text" name="url"
                                       placeholder="输入应用链接(必填)">
                            </div>
                        </label>
                    </div>
                </div>
            </div>
            <div class="modal-bottom">
                <button data-type="1" data-confirm class="form-editor add btn">确定</button>
            </div>
        </div>
    </t:templateContent>

</t:templatePage>