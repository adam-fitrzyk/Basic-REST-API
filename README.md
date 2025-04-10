Version 1.2

# Introduction

This document describes a technical task for candidates applying for Sr Java Software Engineering role at ---. This is programming task to be completed by you at home. We recommend that you time box your activity and priorities based on the highest value added delivery items. 
We want to get a feeling how you name, structure and organise your code as well as how you prioritise. 

# Technical task

The requirement are detailed below. Your tasks is to implement as many as you can in a certain amount of time. We are not asking you to spend days on this exercise. Your application will still be evaluated even if not all requirements are implemented. If you decided to skip or change some requirements please explain your decision.

The tasks is to create a REST API interface which allows client applications to query data from a datastore.

Note: As we are running on OS.X, we will not be able to test your project in a Windows environment. 

## Application
The goal is to develop a (RESTful) API application, lets call it "Search Facade", written in Java 21.
- Should compile and run under JVM "21".
- Use Maven build tool
- Provide the command line to execute the project
- All your tests must pass
- As a nice to have provide a way to launch application as Docker container.

## REST API

The REST API has to run on port `6868`
Implement a `GET /:resource/:id` endpoint where `:id` is value of the `id` attribute of relevant resource and `resource` is an entry from the sample data.
Implement `GET /:resource/search` endpoint. It should require a parameter `filter`, for example:

```
filter1 = ...
filter2 = ...
filter3 = ...
```

`GET /events/search?filter=filter1&filter=filter2&filter=filter3`

In SQL it would be something like `SELECT * FROM events WHERE filter1 AND filter2 AND filter3`

The filter is a JSON object:

```
{
    "attribute": text,   // required
    "operator": text,    // required
    "value": text,       // optional if "range" is provided
    "range": {           // optional if "value" is provided
        "from": text,
        "to": text
    }
}
```

### Operators
Implement the following operators:

- `eq`: equals;
- `gte`: greater than or equals;
- `lte`: less than or equals;

### Validation
- Unknown operators are not allowed.
- Both `value` *and* `range` are not allowed.
- When operator is `gte` *or* `lte` then `range` is not allowed.

### Examples
Having the following data in the database:

```
[
    { "id": 1, "language": "Java", "version": 8, "isJvmBased": "true" },
    { "id": 2, "language": "Java", "version": 7, "isJvmBased": "true" },
    { "id": 3, "language": "Groovy", "version": 3, "isJvmBased": "true" },
    { "id": 4, "language": "Kotlin", "version": 2, "isJvmBased": "true" }
]
```

This query will return all Java objects:

filter1 = `{ "attribute": "language", "operator": "eq", "value": "Java" }`
filter2 = `{ "attribute": "isJvmBased", "operator": "eq", "value": "true" }`

`?filter=filter1&filter=filter2`

This query will return 3 objects with ids 2, 3, 4:

idRange = `{ "attribute": "id", "operator": "eq", "range": { "from": 2, "to": 4 } }`

`?filter=idRange`

## Database
Our preference is a NOSQL database like MongoDB but for simplicity you can choose whatever database works for you.
- Provide a shell script to launch the database server on port `27777` as Docker container.
- Provide a way to upload all JSONs from sample data folder.


## Sample Data
### Events

```

[
    {
        "_id": "507f191e810c19729de8aae0",
        "type": "LOGIN",
        "time": 1262340660000,
        "user": "user1@sample.io",
        "ip": "192.168.1.10"
    },
    {
        "_id": "507f191e810c19729de8aae1",
        "type": "LOGIN",
        "time": 1262427060000,
        "user": "user2@sample.io",
        "ip": "192.168.1.11"
    },
    {
        "_id": "507f191e810c19729de8aae2",
        "type": "LOGIN",
        "time": 1262513460000,
        "user": "user3@sample.io",
        "ip": "192.168.1.11"
    },
    {
        "_id": "507f191e810c19729de8aae3",
        "type": "LOGIN",
        "time": 1262599860000,
        "user": "user4@sample.io",
        "ip": "192.168.1.13"
    },
    {
        "_id": "507f191e810c19729de8aae4",
        "type": "LOGOUT",
        "time": 1262369460000,
        "user": "user1@sample.io",
        "ip": "192.168.1.10"
    },
    {
        "_id": "507f191e810c19729de8aae5",
        "type": "LOGOUT",
        "time": 1262455860000,
        "user": "user2@sample.io",
        "ip": "192.168.1.11"
    },
    {
        "_id": "507f191e810c19729de8aae6",
        "type": "LOGOUT",
        "time": 1262542260000,
        "user": "user3@sample.io",
        "ip": "192.168.1.11"
    },
    {
        "_id": "507f191e810c19729de8aae7",
        "type": "LOGOUT",
        "time": 1262628660000,
        "user": "user4@sample.io",
        "ip": "192.168.1.13"
    }
]
```

### Users

```
[
    {
        "_id": "507f191e810c19729de860e0",
        "user": "user1@sample.io",
        "workstation": "192.168.1.10"
    },
    {
        "_id": "507f191e810c19729de860e1",
        "user": "user2@sample.io",
        "workstation": "192.168.1.11"
    },
    {
        "_id": "507f191e810c19729de860e2",
        "user": "user3@sample.io",
        "workstation": "192.168.1.12"
    },
    {
        "_id": "507f191e810c19729de860e3",
        "user": "user4@sample.io",
        "workstation": "192.168.1.13"
    }
]
```