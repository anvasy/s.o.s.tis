<html>
<head>
    <title>Home</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
</head>
<body>
<h1>Hello! I'm SOSTIS!</h1>

<form name="summaryForm"
      id="summaryForm"
      method="POST"
      action="${pageContext.request.contextPath}/summary"
      enctype="multipart/form-data">
    <label for="document">Document: </label>
    <input type="file" name="document" id="document">
    <button type="submit">GO</button>
</form>

<div id="result">

</div>

<script src="${pageContext.request.contextPath}/restRequests.js"></script>
</body>
</html>
