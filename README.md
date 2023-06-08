make sure to use java17

to run the server - use the run.bat

to config the database -> inside .\db\config.txt change the fields accordingly,
or alternatively, use the sqldump provided.

Note: the values in requests' body does not need to be enclosed with quotation marks.

endpoints:
* /campaigns/create - methods : [POST] 
Note: StartDate Will be created automatically when a new entry is submitted with current date.
use to create a campaign, body expected to contain json as follows:

  [Body]

      {  
          Name=name,

          Products=[1,2,3] (list of identifiers (integers) of products in the campaign the ids shpuld be found in the database)

          Bid=(a decimal number representing PPC) 
      }
   
  [Body]
  
* /ads/retrieve - methods : [GET]
 use to retrieve ads 
 
   [query params]

      specify the request category /ads/retrieve?cat=category

      for instance if the category 'tennis' is desired:

      /ads/retrieve?cat=tennis

   [query params]
  
* /entities/products - methods : [POST]
  use to insert new products to the db. body expected to contain json as follows:
  
  [Body]
 
       {
            Title=title,

            Category=category,

            Price=(a decimal representing price),

            SerialNumber=(integer representing S\N)
        }
  [Body]
    
<!--     
 * /entities/campaign - methods : [GET]
  used to retrieve campaign details from the db.
  <Body>
    Title=<title>
    Category=<category>
    Price=<a decimal representing price>
  </Body> -->
    
  
  
  
  
 
  
 
 
