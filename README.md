to run the server - use the run.bat

to config the database -> inside .\db\config.txt change the fields accordingly.

endpoints:
* /campaigns/create - methods : [POST] 
used to create a campaign, body expected to contain key-value pairs as follows:

  [Body]

      Name="campaign name"

      StartDate="date to which 10 days from, the campagin will be active"

      Products="comma seperated identifiers (integers) of products in the campaign; the ids shpuld be found in the database"

      Bid="a decimal number representing PPC"

  [Body]
  
* /ads/retrieve - methods : [GET]
 used to retrieve ads 
 
   [query params]

      specify the request category /ads/retrieve?cat="category"

      for instance if the category 'tennis' is desired:

      /ads/retrieve?cat=tennis

   [query params]
  
* /entities/products - methods : [POST]
  used to insert new products to the db. body expected to contain key-value pairs as follows:
  [Body]
  
      Title="title"

      Category="category"

      Price="a decimal representing price"
      
      SerialNumber="integer representing S\N"
    
  [Body]
    
<!--     
 * /entities/campaign - methods : [GET]
  used to retrieve campaign details from the db.
  <Body>
    Title=<title>
    Category=<category>
    Price=<a decimal representing price>
  </Body> -->
    
  
  
  
  
 
  
 
 
