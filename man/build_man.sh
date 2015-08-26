#!/bin/bash

if ! which dokujclient >/dev/null; then
  echo dokujclient must be in your PATH in order to run this script
  exit -1
fi

rm -rf man1
mkdir man1

cat man.header.txt > man1/dokujclient.1
for COMMAND in $(dokujclient --help | grep -v "\[" | grep -v "To get help" | grep -v "Available commands" | grep -v help | sort); do
  echo .br >> man1/dokujclient.1
  echo "\fB$COMMAND\fR" >> man1/dokujclient.1
done

cat man.footer.txt >> man1/dokujclient.1

