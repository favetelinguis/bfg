# bfg

[![Build Status](https://travis-ci.org/favetelinguis/bfg.svg?branch=master)](https://travis-ci.org/favetelinguis/bfg) [![Built with Spacemacs](https://cdn.rawgit.com/syl20bnr/spacemacs/442d025779da2f62fc86c2082703697714db6514/assets/spacemacs-badge.svg)](http://github.com/syl20bnr/spacemacs)

A Clojure library designed to ... well, that part is up to you.
To see roadmap and progress check out backlog.org

## Usage

A configuration file is required named configs/private.edn that has the following format:
{:betfair {:usr "<user>"
           :pwd "<password>"
           :app-key "<app-key>"}}

###Server is implemented using the reloabable workflow of stuart sieera. To launch a dev server:
1. Start a repl
2. Type ```(dev)```
3. Type (reload) in the new ns
4. Starts on 8085

###Run backend tests
lein test

###Make production build of frontend code (obs included with git)
npm run build

###Start dev server of frontend code
npm start (localhost:8084)

###Run frontend tests
npm run test
npm run test:watch

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
