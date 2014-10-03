package ProjectionArchive.lavaGlobe;

public class TestClass1 {
	/*
    Draw a 3D object with triangular or rectangular faces.
    Faces are shaded based on their orientation to the light.
    
    *** The ordering of faces needs to be fixed ***
    
    Create basic shapes using:
        createCuboid(width, height, depth)
        createCylinder(radius, height, resolution)
    
    Move shapes using translate3D(x, y, z, shape)
    Otherwise define faces counterclockwise.
    
    The object can be spun by clicking and dragging.
*/

// Move to centre of screen
translate(200.5, 200.5);
stroke(1, 92, 7);
//noStroke();
var rotateAngle = 2;
var faceColor = [20, 255, 40];

var translate3D = function(x, y, z, shape) {
  var nodes= shape.nodes;
  for (var i = 0; i < nodes.length; i+=1) {
    nodes[i] = [nodes[i][0]+x, nodes[i][1]+y, nodes[i][2]+z];
  }
};

var createTorus = function(r1, n1, r2, n2) {
    // Create a torus along the y-axis, centered on (0,0)
    // The cross-rections has radius r1 and n1 vertices,
    // The torus has radius r2 and n2 cross-sections.
    
    var c, i, j, dx, dy;
    var theta = 360 / n1;
    var nodes = [];
    var node;
    
    for (j = 0; j < n2; j++) {
        c = [r2 * cos(theta * j), 0, r2 * sin(theta * j)];
        
        for (i = 0; i < n1; i++) {
            dx = r1 * cos(theta * i);
            dy = r1 * sin(theta * i);
            node = [c[0] + dx * cos(theta * j),
                    c[1] + dy,
                    c[2] + dx * sin(theta * j)];
            nodes.push(node);
        }
    }

    var faces = [];
    for (j = 0; j < n2-1; j++) {
        var s = j * n1;
        for (i = 0; i < n1; i++) {
            if (i < n1 - 1) {
                faces.push([s+i+1, s+i, s+i+n1, s+i+n1+1]);
            } else {
                faces.push([s+0, s+i, s+i+n1, s+n1]);
            }
        }
        
    }
    return {nodes: nodes, faces: faces};
};

var createCuboid = function(w, h, d) {
    // Create a cuboid centered on (0, 0)
    // With width, w, and height, h, and depth, d.
    
    w /= 2;
    h /= 2;
    d /= 2;
    
    var nodes = [[-w,-h,-d],[w,-h,-d],[w,h,-d],[-w,h,-d],
                 [-w,-h, d],[w,-h, d],[w,h, d],[-w,h, d]];
                 
    var faces = [[0,1,2,3],[7,6,5,4],
                 [0,3,7,4],[2,1,5,6],
                 [3,2,6,7],[4,5,1,0]];
    
    return {nodes: nodes, faces: faces};
};

var createCylinder = function(r, h, n) {
    // Create a cylinder along the z-axis, centered on (0,0)
    // With radius, r, and hegiht, h, and
    // n points along its circumference.
    
    var nodes = [[0, 0, h/2], [0, 0, -h/2]];
    var faces = [];
    var theta = 360 / n;
    
    for (var i = 0; i <= n; i++){
        nodes.push([r*cos(i*theta), r*sin(i*theta), h/2]);
        nodes.push([r*cos(i*theta), r*sin(i*theta), -h/2]);
        if (i < n){
            faces.push([0, i*2+4, i*2+2]);
            faces.push([1, i*2+3, i*2+5]);
            faces.push([i*2+2, i*2+4, i*2+5, i*2+3]);
        }
    }
    
    return {nodes: nodes, faces: faces};
};

/********************************************************
*                   Create Torus Here
* 
********************************************************/
var objects = [createTorus(40, 20, 100, 50)];

var subtractVectors = function(v1, v2){
    return [[v1[0] - v2[0]],
            [v1[1] - v2[1]],
            [v1[2] - v2[2]]];
};

var normaliseVector = function(v) {
    var d = sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);
    return [v[0]/d, v[1]/d, v[2]/d];
};

var normalOfPlane = function(face, nodes) {
    var n1 = nodes[face[0]];
    var n2 = nodes[face[1]];
    var n3 = nodes[face[2]];
    
    var v1 = subtractVectors(n1, n2);
    var v2 = subtractVectors(n1, n3);
    
    var v3 = [[v1[1]*v2[2] - v1[2]*v2[1]],
              [v1[2]*v2[0] - v1[0]*v2[2]],
              [v1[0]*v2[1] - v1[1]*v2[0]]];
              
    return v3;
};

var dotProduct = function(v1, v2){
    // Assume everything has 3 dimensions
    return v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
};

var currentX = false;
var currentY = false;

var rotateY3D = function(theta, nodes) {
    var ct = cos(theta);
    var st = sin(theta);
    var x, y, z;

    for (var i = 0; i < nodes.length; i+=1) {
        x = nodes[i][0];
        y = nodes[i][1];
        z = nodes[i][2];
        nodes[i] = [ct*x + st*z, y, -st*x + ct*z];
    }
};

var rotateX3D = function(theta, nodes){
    var ct = cos(theta);
    var st = sin(theta);
    var x, y, z;
    var obj;
    
    for (var i = 0; i < nodes.length; i+=1) {
        x = nodes[i][0];
        y = nodes[i][1];
        z = nodes[i][2];
        nodes[i] = [x, ct*y - st*z, st*y + ct*z];
    }
};

var sortFunction = function(a, b) {
    return a[0] - b[0];
};

var lightVector = normaliseVector([1,0,-2]);

var draw = function() {
    var i, o, f;
    var face;
    var faces = [];
    var nodes;
    var node1;
    var node2;
    
    background(255, 255, 255);

    for (o in objects) {
        var obj = objects[o];
        nodes = obj.nodes;
        
        if ('faces' in obj) {
            for (f in obj.faces) {
                face = obj.faces[f];
                var fnorm = normalOfPlane(face, nodes);
                
                if (fnorm[2] < 0) {
                    // Find order in which to draw faces
                    // by finding where it intersects the z-axis
                    var pos = dotProduct(fnorm, nodes[face[0]]);
                    
                    var fnodes;
                    if (face.length === 3) {
                        fnodes = [nodes[face[0]], nodes[face[1]],
                                  nodes[face[2]]];
                    } else {
                        fnodes = [nodes[face[0]], nodes[face[1]],
                                  nodes[face[2]], nodes[face[3]]];
                    }
                    
                    faces.push([pos / fnorm[2], fnorm, fnodes]);
                }
            }
        }
        
        if ('edges' in obj) {
            var edges = obj.edges;
            for (i = 0; i < edges.length; i+=1) {
                node1 = nodes[edges[i][0]];
                node2 = nodes[edges[i][1]];
                line(node1[0], node1[1], node2[0], node2[1]);
            }     
        }
    }
    
    faces.sort(sortFunction);
    
    for (f in faces) {
        face = faces[f];
        nodes = face[2];
        var l = dotProduct(lightVector, normaliseVector(face[1]));   
        fill(l * faceColor[0], l * faceColor[1], l * faceColor[2]);
        
        if (face[2].length === 3) {
            triangle(nodes[0][0], nodes[0][1],
                     nodes[1][0], nodes[1][1],
                     nodes[2][0], nodes[2][1]);
        } else {
            quad(nodes[0][0], nodes[0][1],
                 nodes[1][0], nodes[1][1],
                 nodes[2][0], nodes[2][1],
                 nodes[3][0], nodes[3][1]);
          }
    }

    if (mouseIsPressed) {
        if (currentX !== false) {
            var theta1 = -(currentX - mouseX) * PI / 10;
            var theta2 = (currentY - mouseY) * PI / 10;
            
            for (o in objects) {
                nodes = objects[o].nodes;
                rotateY3D(theta1, nodes);
                rotateX3D(theta2, nodes);    
            }
        }
        currentX = mouseX;
        currentY = mouseY;
    } else {
        currentX = false;
    }
};

var mouseOut = function(){
    mouseIsPressed = false;
};

var keyPressed = function() {
    var f = 0;
    var d = 0;
    if (keyCode === LEFT) {
        f = rotateY3D;
        d = 2;
    } else if (keyCode === RIGHT) {
        f = rotateY3D;
        d = -2;
    } else if (keyCode === UP) {
        f = rotateX3D;
        d = -2;
    } else if (keyCode === DOWN) {
        f = rotateX3D;
        d = 2;
    }
    
    if (f !== 0) {
        for (var obj in objects) {
                f(d, objects[obj].nodes); 
        }
    }
    
};
}
