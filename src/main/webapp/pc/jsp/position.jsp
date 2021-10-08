<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" uri="http://www.0512.red/tags" %>
<t:templatePage id="pc-home">

    <t:templateContent id="head">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/pc/css/jquery.tagsinput-revisited.css">
        <script src="${pageContext.request.contextPath}/pc/js/jquery.tagsinput-revisited.js"></script>
        <script>
            let table, addModal;
            home.on("init", function () {
                table = $("#table").table({
                    url: "/sys/position/data",
                    columns: [
                        [
                            {title: "序号", field: "order", align: "center", width: "5%"},
                            {title: "职位名称", field: "name", align: "left", width: "25%"},
                            {
                                title: "已分配人员", field: "users", align: "left", width: "50%",
                                formatter: function (index, field, rowData) {
                                    let users = rowData.users;
                                    if (!users || users.length === 0) {
                                        return "";
                                    }
                                    let names = [];
                                    for (let i = 0; i < users.length; i++) {
                                        names.push(users[i].name);
                                    }
                                    return names.join("、");
                                }
                            },
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
                                    if (rowData.id === 1) {
                                        operate.eq(1).hide();
                                    }else{
                                        operate.eq(1).on("click", function () {
                                            remove(rowData);
                                        });
                                    }
                                    return operate;
                                }
                            }
                        ]
                    ]
                });
                $("#add").on("click", add);
                addModal = $("#module-add-modal").modal({
                    initOpen: false,
                    onConfirm: function () {
                        let id = addModal.data("id");
                        let name = addModal.find("input[name='name']").val();
                        if (!name) {
                            $.alert("职位名称不能为空");
                            return;
                        }
                        let users = addModal.find("input[name='users']").val();
                        let persons = [];
                        if (users) {
                            let split = users.split(",");
                            for (let i = 0; i < split.length; i++) {
                                persons.push({
                                    id: split[i]
                                });
                            }
                        }
                        let position = {
                            id: id,
                            name: name,
                            users: persons
                        };
                        $.ajax({
                            type: "post",
                            url: "/sys/position/" + (position.id ? "update" : "add"),
                            data: JSON.stringify(position),
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
                addModal.find("input[name='users']").tagsInput({
                    placeholder: '添加人员',
                    width: '100%',
                    minHeight: '140px',
                    defaultText: "添加人员",
                    tagColor: "#0079ff",
                    autocomplete: {
                        idField: "id",
                        textField: "name",
                        source: '/sys/position/searchPersons',
                        minLength: 0
                    }
                });
            });

            function add() {
                addModal.find("input[name='name']").val("");
                addModal.find("input[name='users']").val("").clearTag();
                addModal.data("id", "").modal("open");
            }

            function edit(rowData) {
                addModal.find("input[name='name']").val(rowData.name);
                let usersInput = addModal.find("input[name='users']");
                usersInput.val("").clearTag();
                let users = rowData.users;
                if (users && users.length > 0) {
                    for (let i = 0; i < users.length; i++) {
                        usersInput.addTag({
                            id: users[i].id + "",
                            value: users[i].name
                        }, {
                            focus: true,
                            color: "#0079ff"
                        });
                    }
                }
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
                                url: "/sys/position/delete",
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
                <span class="modal-title">添加新职位</span>
                <div class="modal-close">x</div>
            </div>
            <div class="modal-body">
                <div class="user-info-container">
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">职位名称 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="20" type="text" name="name"
                                       placeholder="输入职位名称(必填)">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <label class="edit-wrapper">
                            <span class="edit-title">分配人员 :</span>
                            <div class="edit-value">
                                <input class="edit-control" type="text" name="users">
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