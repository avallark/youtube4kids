{% extends "templates/base.html" %}
{% block content %}
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-12">
            <!-- Widgets start here -->
            <div class="ibox float-e-margins">
                <h4>Todays list of Doctors</h4>
                <table class="table table-condensed table-striped table-bordered">
                    <tr>
                        <th>Doctor's Name</th>
                        <th>Address</th>
                        <th>Mark Seen</th>
                    </tr>
                    {% for td in todays-doctors %}
                    <tr>
                        <td><a href="/doctors/{{td.doctor-id}}/edit"> {{td.doctor-name}}</a></td>
                        <td> {{td.doctors-address}}</td>
                        <!--<td><a id="call-button" href="" class="btn btn-info"> Mark Seen </a></td>-->
                        <td>
                            <div id="{{td.doctor-id}}-call-seen-button" class="text-center">
                                <a onclick="getLocation()" data-toggle="modal" href="#modal-form" class="btn btn-primary">Call Seen</a>
                            </div>
                            <div style="display:none" id="{{td.doctor-id}}-call-seen-already">
                                Call marked.
                            </div>
                            <div id="modal-form" class="modal fade" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-body">
                                            <div class="row">
                                                <div class="col-sm-12">
                                                    <h3 class="m-t-none m-b">Call Details</h3>
                                                    <p>Enter any comments you want to save and refer later.</p
                                                    <form class="modal-class-form" role="form" id="{{td.doctor-id}}-call-form" action="/boo">
                                                        <input type="hidden" name="call-sales-rep-user-id" id="{{td.doctor-id}}-call-sales-rep-user-id" value="{{list-user.user-id}}">
                                                        <input type="hidden" name="call-doctor-id" id="{{td.doctor-id}}-call-doctor-id" value="{{td.doctor-id}}">
                                                        <input type="hidden" name="created-by" id="{{td.doctor-id}}-created-by" value="{{req.session.user.user-id}}">
                                                        <div class="form-group">
                                                            <label>Notes</label>
                                                            <textarea class="form-control" name="call-comments" id="{{td.doctor-id}}-call-comments"></textarea>
                                                        </div>
                                                        <div class="col-sm-12">
                                                            <h3 class="m-t-none m-b">Products Detailed</h3>
                                                            {% for i in items %}
                                                            <div class="col-sm-6">
                                                                <label class="checkbox-inline i-checks">
                                                                    <div class="icheckbox_square-green" style="position: relative;">

                                                                      <input data-item-id="{{i.item-id}}" type="checkbox" id="{{td.doctor-id}}-{{i.item-id}}-item-checkbox" name="{{i.item-id}}-item-checkbox">
                                                                    </div>
                                                                    {{i.item-shortname}}
                                                                </label>
                                                            </div>
                                                            {% endfor %}
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-primary" onclick="saveCallDetails({{td.doctor-id}})">Save changes</button>
                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    {% endfor %}
                </table>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript" src="static/js/src/home.js"></script>
<script>
 $( document ).ready(function() {
     getLocation();
 });
 $(".modal-class-form").on("submit", function(event) {
     event.preventDefault();
     console.log( "default " + event.type + " prevented" );
 });
</script>
{% endblock %}
