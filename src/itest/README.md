Running integration tests
=========================

Tests will try to connect to an instance of Dokuwiki.
To set up such an instance you should run the installTestEnvironment.sh script.
This script is meant to be run from src/itest (otherwise it may not find some file it needs)
You may need to run it as root since it will try to write files in your server filesystem and
give ownership to its user.
