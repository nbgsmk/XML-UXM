# UXM is Universal Xml Mechanic
This is a highly specialized tool for automated editing of XML config files for NOKIA RAN nodes.
RAN stands for Radio Access Network ie mobile network controllers and base stations.

UXM uses a proprietary scripting "language" to define rules for automated xml editing.

In short terms, UXM is supposed to:
- read rules from csv
- read input data from Excel
- read input xml (original to be edited)

- based on rules:
  - find appropriate fields in Excel
  - read their values and prepare appropriately
  - find destination element(s) in the original xml
  - modify accordingly, applying data prepared from excel 

- finally, write modified xml structure into output file


Rules are:
- specified in simple csv format
- "One time" - applied only once from input excel to destination xml.
- "Mult" - applied from input excel to several elements in the destination xml. 
- Unlimited length - there can be any number of rules.
- Unlimited depth - xml structure can be nested and edited in depth without restrictions.




