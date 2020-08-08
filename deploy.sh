#!/bin/bash

cd heroku/zwergigel
rm -rf src
echo Previous version was removed
cp -r ../../backend/zwergigel/src .
echo Backend was copied
cd ../../frontend/zwergigel
lein clean
lein package
cp public/index.html ../../heroku/zwergigel/src/main/resources/static/
cp -r public/css/ ../../heroku/zwergigel/src/main/resources/static/
cp -r public/js/ ../../heroku/zwergigel/src/main/resources/static/
echo Frontend was copied
cd ../../heroku/zwergigel
git status
git add .
git commit -m "New changes"
git push heroku master
echo Done.

