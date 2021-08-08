<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" uri="http://www.0512.red/tags" %>
<t:templatePage id="pc-home">

    <t:templateContent id="title">
        首页
    </t:templateContent>

    <t:templateContent id="body">
        <div class="penal-header">
            <button class="form-editor add2">
                <i class="iconfont icon-add-s"></i>
                添加
            </button>
        </div>
        <div class="penal-body">
            <div class="custom-table">
                <table class="table-header">
                    <thead>
                    <tr>
                        <th style="width: 25%">大苏打</th>
                        <th style="width: 25%">大撒大撒</th>
                        <th style="width: 25%">大撒大撒</th>
                        <th style="width: 25%">大撒大撒</th>
                    </tr>
                    </thead>
                </table>
                <table class="table-body">
                    <table class="table-header">
                        <tbody>
                        <tr>
                            <td style="width: 25%">1</td>
                            <td style="width: 25%">2</td>
                            <td style="width: 25%">3</td>
                            <td style="width: 25%">4</td>
                        </tr>
                        <tr>
                            <td style="width: 25%">1</td>
                            <td style="width: 25%">2</td>
                            <td style="width: 25%">3</td>
                            <td style="width: 25%">4</td>
                        </tr>
                        <tr>
                            <td style="width: 25%">1</td>
                            <td style="width: 25%">2</td>
                            <td style="width: 25%">3</td>
                            <td style="width: 25%">4</td>
                        </tr>
                        <tr>
                            <td style="width: 25%">1</td>
                            <td style="width: 25%">2</td>
                            <td style="width: 25%">3</td>
                            <td style="width: 25%">4</td>
                        </tr>
                        <tr>
                            <td style="width: 25%">1</td>
                            <td style="width: 25%">2</td>
                            <td style="width: 25%">3</td>
                            <td style="width: 25%">4</td>
                        </tr>
                        </tbody>
                    </table>
                </table>
                <div class="table-bottom">
                    <label class="page-rows">
                        <span>每页条数</span>
                        <select>
                            <option>10</option>
                            <option>20</option>
                        </select>
                    </label>
                    <span>
                        第<span>1</span>页（共<span>2</span>页 10条数据）
                    </span>
                    <ul class="pager">
                        <li class="first">上一页</li>
                        <li class="active">1</li>
                        <li>1</li>
                        <li>...</li>
                        <li>1</li>
                        <li>1</li>
                        <li class="last">下一页</li>
                    </ul>
                </div>
            </div>
        </div>
    </t:templateContent>

</t:templatePage>