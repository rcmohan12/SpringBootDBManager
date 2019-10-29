<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<div class="container">
	</div>
	
	<br>
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3>List of Connection's</h3>
		</div>
		<div class="panel-body">
			<table >
				<thead >
					<tr>
						<th width="40%">Hostname</th>
						<th width="20%">Port</th>
						<th width="20%">Database Name</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${connections}" var="connection">
						<tr>
							<td>${connection.hostname}</td>
							<td>${connection.port}</td>
							<td>${connection.dbName}</td>
							<td><a type="button" class="btn btn-success"
								href="/update-connection?id=${connection.id}">Update</a>
							<a type="button" class="btn btn-warning"
								href="/delete-connection?id=${connection.id}">Delete</a>
							<a type="button" class="btn btn-success"
								href="/conect-to-DB?id=${connection.id}">Connect</a></td>
						</tr>
						<tr class="blank_row">
    						<td colspan="3"></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div style = "text-align: center">
		<a type="button" class="btn btn-primary btn-md" href="/add-connection">Add Connection</a>
	</div>

</div>
<%@ include file="common/footer.jspf"%>