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
                    url: "/sys/user/data",
                    columns: [
                        [
                            {title: "序号", field: "order", align: "center", width: "5%"},
                            {title: "用户名", field: "account", align: "left", width: "10%"},
                            {title: "姓名", field: "name", align: "left", width: "10%"},
                            {title: "手机", field: "phone", align: "left", width: "15%"},
                            {title: "邮箱", field: "email", align: "left", width: "15%"},
                            {
                                title: "职位", field: "positions", align: "left", width: "25%",
                                formatter: function (index, field, rowData) {
                                    let positions = rowData.positions;
                                    if (!positions || positions.length === 0) {
                                        return "";
                                    }
                                    let names = [];
                                    for (let i = 0; i < positions.length; i++) {
                                        names.push(positions[i].name);
                                    }
                                    return names.join("、");
                                }
                            },
                            {
                                title: "操作",
                                field: "freeze",
                                align: "center",
                                width: "20%",
                                formatter: function (index, field, rowData) {
                                    if(rowData.account==="admin"){
                                        return '';
                                    }
                                    let freeze = rowData.freeze;
                                    let operate = $('<button class="form-editor update">修改</button>' +
                                        '<button class="form-editor delete">删除</button>' +
                                        '<button class="form-editor ' + (freeze ? 'add' : 'update') + '">' + (freeze ? '解冻' : '冻结') + '</button>');
                                    operate.eq(0).on("click", function () {
                                        edit(rowData);
                                    });
                                    operate.eq(1).on("click", function () {
                                        remove(rowData);
                                    });
                                    let freezeState = false;
                                    let freezeBtn = operate.eq(2).on("click", function () {
                                        if (freezeState) {
                                            return;
                                        }
                                        freezeState = true;
                                        let freeze = freezeBtn.hasClass("update");
                                        $.ajax({
                                            type: "post",
                                            url: "/sys/user/freeze",
                                            data: {
                                                id: rowData.id,
                                                freeze: freeze
                                            },
                                            dataType: "json",
                                            success: function (msg) {
                                                freezeState = false;
                                                if (msg.errorCode === 0) {
                                                    freezeBtn.attr("class", "form-editor " + (freeze ? "add" : "update")).text(freeze ? '解冻' : '冻结');
                                                    $.alert("已"+(!freeze ? '解冻' : '冻结')+"“" + rowData.name + "”");
                                                } else {
                                                    $.alert((!freeze ? '解冻' : '冻结')+"“" + rowData.name + "”失败");
                                                }
                                            }
                                        });
                                    });
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
                        let account = addModal.find("input[name='account']").val().trim();
                        if (!account) {
                            $.alert("用户名不能为空");
                            return;
                        }
                        let password = addModal.find("input[name='password']").val().trim();
                        if (!id && !password) {
                            $.alert("密码不能为空");
                            return;
                        }
                        if (password && password.length < 6) {
                            $.alert("密码不能少于6位");
                            return;
                        }
                        let name = addModal.find("input[name='name']").val().trim();
                        if (!name) {
                            $.alert("姓名不能为空");
                            return;
                        }
                        let phone = addModal.find("input[name='phone']").val().trim();
                        let email = addModal.find("input[name='email']").val().trim();
                        let positions = addModal.find("input[name='positions']").val();
                        let _positions = [];
                        if (positions) {
                            let split = positions.split(",");
                            for (let i = 0; i < split.length; i++) {
                                _positions.push({
                                    id: split[i]
                                });
                            }
                        }
                        let user = {
                            id: id,
                            account: account,
                            password: password,
                            name: name,
                            phone: phone,
                            email: email,
                            positions: _positions
                        };
                        $.ajax({
                            type: "post",
                            url: "/sys/user/" + (user.id ? "update" : "add"),
                            data: JSON.stringify(user),
                            dataType: "json",
                            contentType: "application/json",
                            success: function (msg) {
                                if (msg.errorCode === 0) {
                                    addModal.modal("close");
                                    $.alert((user.id ? "修改" : "添加") + "成功");
                                    table.table("requestData");
                                } else {
                                    $.alert(msg.msg || ((user.id ? "修改" : "添加") + "失败"));
                                }
                            }
                        });
                    }
                });
                addModal.find("input[name='positions']").tagsInput({
                    placeholder: '添加职位(多选)',
                    width: '100%',
                    minHeight: '140px',
                    defaultText: "添加职位",
                    tagColor: "#556270",
                    autocomplete: {
                        idField: "id",
                        textField: "name",
                        source: '/sys/user/searchPositions',
                        minLength: 0
                    }
                });
            });

            function add() {
                addModal.find("input[name='account']").removeAttr("disabled").val("");
                addModal.find("input[name='password']").val("");
                addModal.find("input[name='name']").val("");
                addModal.find("input[name='phone']").val("");
                addModal.find("input[name='email']").val("");
                addModal.find("input[name='positions']").val("").clearTag();
                addModal.data("id", "").modal("open");
            }

            function edit(rowData) {
                addModal.find("input[name='account']").attr("disabled", true).val(rowData.account);
                addModal.find("input[name='password']").val("");
                addModal.find("input[name='name']").val(rowData.name);
                addModal.find("input[name='phone']").val(rowData.phone);
                addModal.find("input[name='email']").val(rowData.email);
                let usersInput = addModal.find("input[name='positions']");
                usersInput.val("").clearTag();
                let positions = rowData.positions;
                if (positions && positions.length > 0) {
                    for (let i = 0; i < positions.length; i++) {
                        usersInput.addTag({
                            id: positions[i].id + "",
                            value: positions[i].name
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
                                url: "/sys/user/delete",
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
                <span class="modal-title">添加新人员</span>
                <div class="modal-close">x</div>
            </div>
            <div class="modal-body">
                <div class="user-info-container">
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">用户名 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="20" type="text" name="account"
                                       placeholder="输入用户名(必填)">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">密<span class="word-hold h14"></span>码 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="20" type="password" name="password"
                                       placeholder="输入密码(最少6位)">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">姓<span class="word-hold h14"></span>名 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="20" type="text" name="name"
                                       placeholder="输入姓名(必填)">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">手<span class="word-hold h14"></span>机 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="20" type="text" name="phone"
                                       placeholder="输入手机号码">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="edit-wrapper">
                            <span class="edit-title">邮<span class="word-hold h14"></span>箱 :</span>
                            <div class="edit-value">
                                <input class="edit-control" maxlength="20" type="text" name="email"
                                       placeholder="输入电子邮箱">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <label class="edit-wrapper">
                            <span class="edit-title">职<span class="word-hold h14"></span>位 :</span>
                            <div class="edit-value">
                                <input class="edit-control" type="text" name="positions">
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