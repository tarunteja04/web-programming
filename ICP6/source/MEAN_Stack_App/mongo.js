var MongoClient = require('mongodb').MongoClient;
var mongo = require('mongodb')
var assert = require('assert');
var bodyParser = require("body-parser");
var express = require('express');
var cors = require('cors');
var app = express();

var url='mongodb://ktarunteja:ravikiran1@ds119273.mlab.com:19273/ktarunteja';//adding the url replacing with credentials
var ObjectID = require('mongodb').ObjectID;

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.post('/create', function (req, res) {
    MongoClient.connect(url, function(err, db) {
        if(err)
        {
            res.write("Failed, Error while connecting to Database");
            res.end();
        }
        insertDocument(db, req.body, function() {
            res.write("Successfully inserted");
            res.end();
        });
    });
});

app.get('/get', function (req, res) {
    MongoClient.connect(url, function(err, db) {
        if(err)
        {
            res.write("Failed, Error while connecting to Database");
            res.end();
        }

        db.collection('books').find().toArray(function(err, result){
            if(err)
            {
                res.write("get Failed");
                res.end();
            }else
            {
                console.log(result);
                res.send(JSON.stringify(result));
            }
            console.log("Got All Documents");

        });
    });

});

app.get('/delete/:_id', function (req, res) {
    MongoClient.connect(url, function(err, db) {
        if(err)
        {
            res.write("Failed, Error while connecting to Database");
            res.end();
        }
        deleteDocument(db, req.params._id, function () {
            res.write("Successfully deleted");
            res.end();
        });
    });
});


app.get('/update/:_id', function (req, res) {
    MongoClient.connect(url, function(err, db) {
        if(err)
        {
            res.write("Failed, Error while connecting to Database");
            res.end();
        }
        var idToBeUpdated = req.params._id;
        var book = req.query;
        console.log("Book Name"+idToBeUpdated);
        var bookData = { $set: { bookName : book.bookName, authorName: book.authorName, ISBN : book.ISBN }};
        updateDocument(db, idToBeUpdated, bookData, res, function () {
            res.write("Successfully updated");
            res.end();
        });
    });
});
//calling the delete document function
var deleteDocument = function (db, id, callback) {
    db.collection('books').deleteOne( {_id : new mongo.ObjectId(id)}, function(err, result) {
        if (err) {
            res.write("Delete Failed, Error While Deleting");
            res.end();
        }
        console.log("Deleted a document from the books collection.");
        callback();
    });
};
//calling the update document function
var updateDocument = function (db, id, book, res, callback) {
    console.log("Id "+id);
    db.collection('books').updateOne( {_id : new mongo.ObjectId(id)}, book, function(err, result) {
        if (err) {
            res.write("Update Failed, Error While Updating");
            res.end();
        }
        console.log("Updated a document in the books collection.");
        callback();
    });
};

var insertDocument = function(db, data, callback) {
    db.collection('books').insertOne( data, function(err, result) {
        if(err)
        {
            res.write("Registration Failed, Error While Registering");
            res.end();
        }
        console.log("Inserted a document into the books collection.");
        callback();
    });
};

var server = app.listen(8081, function () {
    var host = server.address().address;
    var port = server.address().port;

    console.log("Example app listening at http://%s:%s", host, port)
});