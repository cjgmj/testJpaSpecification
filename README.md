# testJpaSpecification
[QueryFilterOrder](https://github.com/cjgmj/testJpaSpecification/blob/master/src/main/java/com/cjgmj/testJpaSpecification/service/filter/QueryFilterOrder.java) is a class to get dynamic queries using JpaSpecificationExecutor into repository. It is a generic class, so you can use it in your own projects. I'm using Lombok so if you are not using it, you will need change some clases to create default constructor, constructor with all arguments and getters / setters.

----
## Necessary classes
* [FilterRequest](https://github.com/cjgmj/testJpaSpecification/blob/master/src/main/java/com/cjgmj/testJpaSpecification/filter/FilterRequest.java)
 * [PaginationRequest](https://github.com/cjgmj/testJpaSpecification/blob/master/src/main/java/com/cjgmj/testJpaSpecification/filter/PaginationRequest.java)
 * [SearchRequest](https://github.com/cjgmj/testJpaSpecification/blob/master/src/main/java/com/cjgmj/testJpaSpecification/filter/SearchRequest.java)
 * [OrderRequest](https://github.com/cjgmj/testJpaSpecification/blob/master/src/main/java/com/cjgmj/testJpaSpecification/filter/OrderRequest.java)
* [DateFilter](https://github.com/cjgmj/testJpaSpecification/blob/master/src/main/java/com/cjgmj/testJpaSpecification/util/DateFilter.java)

----
## Usage
1. Import JpaSpecificationExecutor in the repository.
2. As QueryFilterOrder is a Component, it can be autowired bySpring's dependency injection.
3. Create a method to get fields from request and map it to object's attribute, it will be the fisrt param from method.
4. Create a method to get all object's attributes which you want filter.
5. Create a method to get the object's attribute which will filter between two dates.
6. Get a Specification using method 'filter' from queryFilterOrder using the methods created at step 3, 4 and 5.

You can see an example in [PersonServiceImpl](https://github.com/cjgmj/testJpaSpecification/blob/master/src/main/java/com/cjgmj/testJpaSpecification/service/impl/PersonServiceImpl.java), also you can set null the methods from step 4 and 5, like you can see in [ReservationServiceImpl](https://github.com/cjgmj/testJpaSpecification/blob/master/src/main/java/com/cjgmj/testJpaSpecification/service/impl/ReservationServiceImpl.java).

----
## Request example
It will be POST to use FilterRequest. Here is an example of the object:

    {
        "page": {
            "page": 0,
            "pageSize": 5
        },
        "search": [{
	        "field": null,
	        "value": null
        }],
        "order": [{
	        "field": null,
	        "sort": null
        }],
        "globalSearch": null
    }

Field can be set with any string, but you should map it at step 3, 4 and 5 with the object's attribute. If the attribute is a join you should concat the string with '.', in [ReservationServiceImpl](https://github.com/cjgmj/testJpaSpecification/blob/master/src/main/java/com/cjgmj/testJpaSpecification/service/impl/ReservationServiceImpl.java) you can see an example.