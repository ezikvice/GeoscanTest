<%@page session="false" language="java" contentType="text/html; UTF-8"
    pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ru">
<head>
<title>Test task for GeoScan</title>

<c:url var="home" value="/" scope="request" />

<spring:url value="/resources/core/css/bootstrap.min.css"
	var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />

<spring:url value="/resources/core/js"
			var="JsPath" />

<spring:url value="${JsPath}/jquery.1.10.2.min.js"
	var="jqueryJs" />
<script src="${jqueryJs}"></script>
<spring:url value="${JsPath}/three.min.js"
			var="threeJs" />
<script src="${threeJs}"></script>
<spring:url value="${JsPath}/OrbitControls.js"
			var="OrbitControlsJs" />
<script src="${OrbitControlsJs}"></script>
<spring:url value="${JsPath}/drawFunctions.js"
			var="drawFunctionsJs" />
<script src="${drawFunctionsJs}"></script>
<spring:url value="${JsPath}/jquery.inputmask.bundle.min.js"
			var="jqueryInputMaskJs" />
<script src="${jqueryInputMaskJs}"></script>

</head>

<nav class="navbar navbar-inverse">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">Cubarium. Test task for GeoScan</a>
		</div>
	</div>
</nav>

<div class="container" style="min-height: 500px">

	<div class="panel panel-info">
		<div class="panel-heading">
			<h3 class="panel-title"><span class="label label-info">Info</span> Важная информация</h3>
		</div>
		<div class="panel-body"><p>Допустимо вводить только натуральные числа через запятую.</p>
			<p>Числа больше 100 будут усечены до 100</p></div>
	</div>

	<div class="starter-template">

		<form class="form-horizontal" id="search-form">
			<div class="form-group form-group-lg">
				<label class="col-sm-2 control-label">список высот</label>
				<div class="col-sm-10">
					<input type=text data-inputmask-regex="[\d,]+" class="form-control" id="username">
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" id="bth-search"
						class="btn btn-primary btn-lg">Поехали!</button>
				</div>
			</div>
		</form>
		<div id="rndr"></div>
		<div id="feedback"></div>

	</div>

</div>

<div class="container">
	<footer>
		<p>
			&copy; <a href="http://www.mkyong.com">Mkyong.com</a> 2015, edited by Dmitry 2017
		</p>
	</footer>
</div>

<script>
	jQuery(document).ready(function($) {


        // Simple input mask. Allows only cyphers and ","
	    $("#username").inputmask("Regex");

		$("#search-form").submit(function(event) {

			// Disble the search button
			enableSearchButton(false);

			// Prevent the form from submitting via the browser.
			event.preventDefault();

			searchViaAjax();

		});

		function searchViaAjax() {

			var search = {};
            var bricks_array = new Array();

			if($("#username").val().length>0) {
                // SIMPLE
                var bricks_arr = $("#username").val().split(",");
                var bricks_filtered = bricks_arr.filter(function (str) {
                    return str !== ""
                }).map(Number);
                for (var i = 0; i < bricks_filtered.length; ++i) {
                    if (bricks_filtered[i] < 0) {
                        bricks_filtered[i] = 0;
                    }
                    if (bricks_filtered[i] > 100) {
                        bricks_filtered[i] = 100;
                    }
                }
//            var brick_sliced = bricks_filtered.slice(0,100);
                bricks_filtered.splice(100);


                bricks_array = JSON.parse("[" + bricks_filtered + "]");
            }else{
				randomize(bricks_array);
			}
            search["data"] = bricks_array;

            $.ajax({
				type : "POST",
				contentType : "application/json",
				url : "${home}search/api/getSearchResult",
				data : JSON.stringify(search),
				dataType : 'json',
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS: ", data);
					display(data);

					var water_array = data.result;
					drawScene(bricks_array, water_array);
//					animate();

				},
				error : function(e) {
					console.log("ERROR: ", e);
					displayErr(e);
//                    var err = '<div class="alert alert-danger">' +
//                        "<strong>Ой!</strong> Произошла какая-то ошибка. Возможно, вы заполнили неправильно список высот:" +
//                        '<pre>'+ e.responseJSON.msg +'</pre></div>';
//                    $('#feedback').html(err);
				},
				done : function(e) {
					console.log("DONE");
					enableSearchButton(true);
				}
			});

		}

		function enableSearchButton(flag) {
			$("#btn-search").prop("disabled", flag);
		}

		function display(data) {
			var json = "<h4>Ajax Response</h4>"
					+ JSON.stringify(data, null, 4);
			$('#feedback').html(json);
		}

        function displayErr(e) {
            var err = '<div class="alert alert-danger">' +
				"<strong>Ой!</strong> Произошла какая-то ошибка. Возможно, вы заполнили неправильно список высот" +
                        '<pre>'+ e.responseJSON.msg +'</pre>' +
				'</div>';
            $('#feedback').html(err);
        }

        function randomize(bricks){
		    var numOfBricks = Math.floor(Math.random() *101);
		    for(var i = 0; i < numOfBricks; i++){
		        bricks[i] = Math.floor(Math.random() *101);
			}
		}

    });
</script>

</body>
</html>