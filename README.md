to run the server - use the run.bat

to config the database -> inside .\db\config.txt change the fields accordingly.

Note: the values in requests' body does not need to be enclosed with quotation marks.

endpoints:
* /campaigns/create - methods : [POST] 
used to create a campaign, body expected to contain key-value pairs seperated by \n as follows:

  [Body]

      Name={campaign name}\n

      StartDate={date to which 10 days from, the campagin will be active}\n // (if not present, Date.Now() will be used)

      Products={comma seperated identifiers (integers) of products in the campaign; the ids shpuld be found in the database}\n

      Bid={a decimal number representing PPC}

  [Body]
  
* /ads/retrieve - methods : [GET]
 used to retrieve ads 
 
   [query params]

      specify the request category /ads/retrieve?cat=category

      for instance if the category 'tennis' is desired:

      /ads/retrieve?cat=tennis

   [query params]
  
* /entities/products - methods : [POST]
  used to insert new products to the db. body expected to contain json as follows:
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
    
  
  
  
  
 
  
 
 
