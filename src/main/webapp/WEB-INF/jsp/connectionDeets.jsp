<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<div class="container">
	<div class="row">
		<div class="col-md-6 col-md-offset-3 ">
			<div class="panel panel-primary">
				<div class="panel-heading">Add Connection Details</div>
				<div class="panel-body">
					<form:form name = "add-Connection" method="post" modelAttribute="connection" action="/add-connection" onsubmit ="return validateFormFields()">
						<fieldset class="form-group">
							<form:input path="id" type="hidden" class="form-control"/>
							<form:errors path="id" cssClass="text-warning" />
						</fieldset>
						
						<fieldset class="form-group">
							<form:label path="hostname">Hostname</form:label>
							<form:input name = "hostname" path="hostname" type="text" class="form-control"
								required="required" />
							<form:errors path="hostname" cssClass="text-warning" />
						</fieldset>

						<fieldset class="form-group">
							<form:label path="port">Port</form:label>
							<form:input name = "port" path="port" type="text" class="form-control"
								required="required" />
							<form:errors path="port" cssClass="text-warning" />
						</fieldset>

						<fieldset class="form-group">
							<form:label path="dbName">Database Name</form:label>
							<form:input name = "dbName" path="dbName" type="text" class="form-control"
								required="required" />
							<form:errors path="dbName" cssClass="text-warning" />
						</fieldset>
						
						<fieldset class="form-group">
							<form:label path="username">User Name</form:label>
							<form:input name = "username" path="username" type="text" class="form-control"
								required="required" />
							<form:errors path="username" cssClass="text-warning" />
						</fieldset>
						
						<fieldset class="form-group">
							<form:label path="password">Password</form:label>
							<form:input name = "password" path="password" type="password" class="form-control"
								required="required" />
							<form:errors path="password" cssClass="text-warning" />
						</fieldset>

						<button type="submit" class="btn btn-success">Save</button>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="common/footer.jspf"%>
