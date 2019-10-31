<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<div class="container">
	</div>
	<br>
	<c:forEach items="${DBDetails.tableColMap}" var="tables">
	<div style = "background: lightgoldenrodyellow; border-radius: 6px; border-color: black;">
		<div>
			<h3 style = "background: lightgoldenrodyellow; border-radius: 6px;">Table Name : ${tables.key}</h3>
		</div>
		<div class="panel-body">
			<table >
				<thead >
					<tr>
						<c:forEach items="${tables.value}" var="columns">
							<c:forEach items="${columns.field}" var="column">
								<th width="20%" style = "background: lightgrey;">${column}</th>
							</c:forEach>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<!-- Map<String, Map<Integer, List<String>>> tableDataMap; -->
					<c:forEach items="${DBDetails.tableDataMap[tables.key]}" var="tableData">
						<tr>
						<c:forEach items="${tableData.value}" var="rowData" varStatus="loop">
							
								<td>${rowData}</td>
							
						</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	</c:forEach>
</div>
<%@ include file="common/footer.jspf"%>