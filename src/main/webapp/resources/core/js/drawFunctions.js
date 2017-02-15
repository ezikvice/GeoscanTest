/**
 * Created by Dmitry on 14.02.2017.
 */
////// show "water pool" scene ///////
function drawScene(bricks_array, water_array){

    var brick_size = 8;

    var bricks_count = bricks_array.length;
    var brick_wall_length = bricks_count*brick_size;


    var camera = new THREE.PerspectiveCamera(90, window.innerWidth / window.innerHeight, 1, 1000);
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


    // loadTextures(brick_texture, water_texture);
    brick_texture = new THREE.TextureLoader().load( "/resources/core/img/cats.png" );//load texture
    water_texture = new THREE.TextureLoader().load( '/resources/core/img/water2.jpg' );

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
