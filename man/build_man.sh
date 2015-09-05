#!/bin/bash

if [ -z $DOKUJCLIENT_MAN_PATH ]; then
  DOKUJCLIENT_MAN_PATH=.
fi

if [ -z "$DOKUJCLIENT_CMD" ]; then
  if ! which dokujclient >/dev/null; then
    echo dokujclient must be in your PATH in order to run this script
    exit -1
  fi
  DOKUJCLIENT_CMD=dokujclient
fi

rm -rf man1
mkdir man1

cat man.header.txt > $DOKUJCLIENT_MAN_PATH/man1/dokujclient.1
for COMMAND in $($DOKUJCLIENT_CMD --help | grep -v "\[" | grep -v "To get help" | grep -v "Available commands" | grep -v help | sort); do
  echo .br >> $DOKUJCLIENT_MAN_PATH/man1/dokujclient.1
  echo "\fB$COMMAND\fR" >> $DOKUJCLIENT_MAN_PATH/man1/dokujclient.1
done

cat man.footer.txt >> $DOKUJCLIENT_MAN_PATH/man1/dokujclient.1

