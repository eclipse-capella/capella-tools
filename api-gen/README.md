# Launch git diff

- On the git repository, run 
   git diff v7.0.0 head > change.txt
   
- on a cmd in this directory, run  
   npm install
   node --max-old-space-size=4096 function.js change.txt

changes.txt will contains mediawiki format of API changes.