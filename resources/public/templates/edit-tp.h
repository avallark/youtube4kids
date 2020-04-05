{% extends "templates/base.html" %}
{% block content %}
<script>
// var belts = [{% for b in belts %} { "working-belt-id": {{b.working-belt-id}}, "working-belt-name": '{{b.working-belt-name}}'}, {% endfor %}];
</script>
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="col-lg-12">
        <div class="text-left m-t-lg">
            <div class="row wrapper border-bottom white-bg page-heading">
                <div class="col-md-8">
                    <h2>Create Tour Programme</h2>
                    <ol class="breadcrumb">
                        <li>
                            <a href="/doctors/list">Tour Programmes</a>
                        </li>
                        <li class="active">
                                <strong>Create TP</strong>
                        </li>
                    </ol>
                </div>
            </div>
            <hr/>
            {% if error|empty? %}
            {% else %}
            <div class="row text-center well">{{error}}</div>
            {% endif %}
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>Create your tour programme for this month</h5>
                </div>
                <div class="ibox-content">
                    <div class="table-responsive">
                        <table id="create-tp-display-table" class="table table-condensed table-striped table-bordered">
                            <thead>
                                <th>Date</th>
                                <th>Belts to visit</th>
                                <th>More belts?</th>
                                <th>Reset Day</th>
                            </thead>
                            <tbody>
                                {% for d in days-of-month %}
                                <form name="create-tp-form" method="POST" role="form" id="create-tp-form-{{d.num}}">
                                    <tr id="belts-row-{{d.num}}">
                                        <td>{{d.num}} - {{d.day-of-the-week}}</td>
                                        {% ifequal d.holiday "Y" %}
                                        <td>Holiday</td>
                                        <td></td>
                                        {% else %}
                                        <td>
                                            <div id="belts-{{d.num}}">
                                                <div class="form-group">
                                                    <label class="control-label">Belt to focus</label>
                                                    <select data-insert-id=0 id="{{d.num}}-working-belt-id" onchange="saveTp({{list-user.user-id}},'{{d.date-on}}', {{d.num}})" name="{{d.num}}-working-belt-id" class="form-control">
                                                        <option value=""></option>
                                                        {% for belt in belts %}
                                                        <option value={{belt.working-belt-id}}>{{ belt.working-belt-name }}</option>
                                                        {% endfor %}
                                                    </select>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div id="{{d.num}}-button-div">
                                                <p> Add another belt to this date</p>
                                                <a onclick="showSelect({{d.num}})" class="btn btn-success"> Add more belts </a>
                                            </div>
                                            <div style="display:none" class="form-group" id="{{d.num}}-select-div-for-button">
                                                <label class="control-label">Second belt</label>
                                                <select data-insert-id=0 id="{{d.num}}-working-belt-id-2" onchange="saveTp2({{list-user.user-id}},'{{d.date-on}}',{{d.num}})" name="{{d.num}}-working-belt-id-2" class="form-control">
                                                    <option value=""></option>
                                                    {% for belt in belts %}
                                                    <option value={{belt.working-belt-id}}>{{ belt.working-belt-name }}</option>
                                                    {% endfor %}
                                                </select>
                                            </div>
                                        </td>
                                        <td>
                                            <p>Reset days TP settings. Re-enter</p>
                                            <button onclick="resetForm('{{d.date-on}}',{{d.num}},{{list-user.user-id}})" type="reset" class="btn btn-white"><i class="fa fa-ban"></i>  Reset </button>
                                        </td>
                                            {% endifequal %}
                                    </tr>
                                </form>
                                {% endfor %}
                            </tbody>
                        </table>
                    </div>
                    <a onclick="saveTpStatus({{month}}, {{year}}, {{list-user.user-id}}, {{req.session.user.user-id}})" class="btn btn-primary">Send for Approval</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/static/js/src/create-tp.js"></script>
<script>
 $(document).ready(function(){
     console.log("Ready. Now loading saved TPs");
     {% for tp in saved-tps %}
     {% if tp.count > 1 %}
     $("#"+{{tp.num}}+"-working-belt-id").val({{tp.working-belt-id.0}});
     $("#"+{{tp.num}}+"-button-div").hide();
     $("#"+{{tp.num}}+"-select-div-for-button").show();
     $("#"+{{tp.num}}+"-working-belt-id-2").val({{tp.working-belt-id.1}});
     {% else %}
     $("#"+{{tp.num}}+"-working-belt-id").val({{tp.working-belt-id}});
     {% endif %}
     {% endfor %}
 });
</script>
{% endblock %}
