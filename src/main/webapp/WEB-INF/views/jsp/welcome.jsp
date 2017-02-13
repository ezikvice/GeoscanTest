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

<spring:url value="/resources/core/js/jquery.1.10.2.min.js"
	var="jqueryJs" />
<script src="${jqueryJs}"></script>
<spring:url value="/resources/core/js/three.js"
			var="threeJs" />
<script src="${threeJs}"></script>
<spring:url value="/resources/core/js/OrbitControls.js"
			var="OrbitControlsJs" />
<script src="${OrbitControlsJs}"></script>

</head>

<nav class="navbar navbar-inverse">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">Cubarium. Test task for GeoScan</a>
		</div>
	</div>
</nav>

<div class="container" style="min-height: 500px">

	<div class="starter-template">

		<form class="form-horizontal" id="search-form">
			<div class="form-group form-group-lg">
				<label class="col-sm-2 control-label">список высот</label>
				<div class="col-sm-10">
					<input type=text class="form-control" id="username">
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



        ////// show "water pool" scene ///////
        function drawScene(bricks_array, water_array){

            var brick_size = 8;

            var bricks_count = bricks_array.length;
            var brick_wall_length = bricks_count*brick_size;


            var camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 1, 800);
            camera.position.set(-brick_wall_length/2, brick_wall_length/2, brick_wall_length);

            var scene = new THREE.Scene();

//            var ambientLight = new THREE.AmbientLight(0x0c0c0c);
//            scene.add(ambientLight);

            //// grid for a convenience
            var plane = new THREE.GridHelper(brick_size*bricks_count, bricks_count);
            scene.add(plane);


            var renderer = renderer = new THREE.WebGLRenderer({ antialias: true });
//        renderer.setSize(window.innerWidth*3/4, window.innerHeight*3/4);
            renderer.setSize(940, 600);
            $("#rndr").empty(); // deleting everything inside div
            $("#rndr")[0].appendChild(renderer.domElement);
		    renderer.setClearColor(0xF0F0FF, 1); // background color


            var controls = new THREE.OrbitControls(camera, renderer.domElement);
//    controls.minPolarAngle = Math.PI / 2; //uncomment for limiting angles
            controls.maxPolarAngle = Math.PI / 2;


            function animate() {
                requestAnimationFrame(animate);
                controls.update();
                renderer.render(scene, camera);
            }



            <spring:url value="/resources/core/img"
            var="img" />
            var brick_texture = new THREE.TextureLoader().load( "${img}/cats.png" );//load texture
            var water_texture = new THREE.TextureLoader().load( '${img}/water2.jpg' );


            for(var i = 0; i < bricks_count; i++){
                if(bricks_array[i]>0) {
                    // bricks
                    for(k = 0; k < bricks_array[i]; k++){
                        var brick_material = new THREE.MeshBasicMaterial({map: brick_texture});
                        var cube_geometry = new THREE.CubeGeometry(brick_size, brick_size, brick_size);
                        var brick = new THREE.Mesh(cube_geometry, brick_material);
                        brick.position.y = brick_size * k + brick_size/2;
                        brick.position.x = i * 8 - brick_size*(bricks_count - 1)/2;
                        scene.add(brick);
                    }
                    //water
                    for (k = bricks_array[i]; k < water_array[i]; k++){
                        var water_material = new THREE.MeshBasicMaterial({map: water_texture});
                        water_material.opacity = 0.5;
                        water_material.transparent = true;
                        var cube_geometry = new THREE.CubeGeometry(brick_size, brick_size, brick_size);
                        var water = new THREE.Mesh(cube_geometry, water_material);
                        water.position.y = brick_size * k + brick_size/2;
                        water.position.x = i * 8 - brick_size*(bricks_count - 1)/2;
                        scene.add(water);
                    }
                }
            }

            animate();
        }
        ////// <-- show "water pool" scene ///////



		$("#search-form").submit(function(event) {

			// Disble the search button
			enableSearchButton(false);

			// Prevent the form from submitting via the browser.
			event.preventDefault();

			searchViaAjax();

		});

		function searchViaAjax() {

			var search = {};
			// TODO: make a simple validation and error handling
            var bricks_array = JSON.parse("[" + $("#username").val() + "]");
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
					display(e);
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

    });
</script>

</body>
</html>