var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var players = [];
var mapElements = [];
var mapUpdated = false;

var vertexList = [];
var edgeList = [];

var hasGameStarted = false;


server.listen(8080,'10.0.0.54', function(){
		console.log("Server is now running...");
});

io.on('connection', function(socket){
	console.log("Player Connected!");
	socket.emit('socketID', { id: socket.id} );
	socket.emit('getPlayers',players);
	socket.emit('mapSync',mapElements);
	
	socket.broadcast.emit('newPlayer', { id: socket.id});
	socket.on('playerInfoUpdate', function(data){
		data.id = socket.id;
		for(var i = 0; i < players.length; i++){
			if(players[i].id==data.id){
				players[i].name = data.name;
				players[i].colorOfPlayer = data.colorOfPlayer;
				players[i].lastDiceRoll = data.diceRoll
				
			}
		}
		socket.broadcast.emit('playerInfoUpdate',data);
	});
	
	socket.on('playerColorNameUpdate', function(data){
		data.id = socket.id;
		for(var i = 0; i < players.length; i++){
			if(players[i].id==data.id){
				players[i].name = data.name;
				players[i].colorOfPlayer = data.colorOfPlayer;
				
			}
		}
		socket.broadcast.emit('playerColorNameUpdate',data);
	});
	socket.on('mapSync', function(data){
		if(!mapUpdated){
			for(var i = 0; i< data.length;i++) {
				mapElements.push(new mapElement(data[i].Type,data[i].Value));
				//console.log(data[i]);
			}
			
			console.log("Map Updated");
			
		}
		mapUpdated=true;
		
	});
	socket.on('startGame', function(data){
		socket.broadcast.emit('startGame');
		hasGameStarted = true;
		console.log("The Game Has Started");
		
	});
	socket.on('gameNextTurn', function(data){
		
		socket.broadcast.emit('gameNextTurn', data);
		console.log("Next Turn");
		
	});
	socket.on('mapUpdateVertex', function(data){
			for(var i = 0; i< data.length;i++) {
				vertexList[i]=new vertex(data[i].BuildingName,data[i].Color);
				//console.log(data[i]);
			}
			socket.broadcast.emit('mapUpdateVertex', vertexList)
			
	});
	socket.on('mapUpdateEdge', function(data){
			for(var i = 0; i< data.length;i++) {
				edgeList[i]= new edge(data[i].BuildingName,data[i].Color);
				//console.log(data[i]);
			}
			socket.broadcast.emit('mapUpdateEdge', edgeList)
			
	});
	socket.on('mapUpdateRobber', function(data){
			socket.broadcast.emit('mapUpdateRobber', data)
			
	});
	socket.on('playerUpdateInv', function(data){
			data.id = socket.id;
			socket.broadcast.emit('playerUpdateInv', data)
			
	});
	socket.on('otherPlayerUpdateInv', function(data){
			socket.broadcast.emit('playerUpdateInv', data)
			
	});
	socket.on('updateCheatCounter', function(data){
			socket.broadcast.emit('updateCheatCounter', data)
			
	});
	socket.on('updateDCardList', function(data){
			socket.broadcast.emit('updateDCardList', data)
			
	});
	socket.on('disconnect', function(){
		console.log("Player Disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id});
		for(var i = 0; i < players.length; i++){
			if(players[i].id == socket.id){
				players.splice(i,1);
			}
		}
	});
	players.push(new player(socket.id, "defaultName", "Black",0));
});

function player(id,name,colorOfPlayer,lastDiceRoll){
	this.id = id;
	this.name = name;
	this.colorOfPlayer = colorOfPlayer;
	this.lastDiceRoll = lastDiceRoll;
}

function mapElement(Type,Value){
	this.Type = Type;
	this.Value = Value;
}

function vertex(BuildingName, Color){
	this.BuildingName=BuildingName;
	this.Color=Color;
	
}
	
function edge(BuildingName, Color){
	this.BuildingName=BuildingName;
	this.Color=Color;
}