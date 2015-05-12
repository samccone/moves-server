# moves-server

A [Moves](https://www.moves-app.com/) [web authentication](https://dev.moves-app.com/docs/authentication) flow server.

## Usage

First setup your profiles.clj as follows

```clj
{:dev {:env {
             :client-secret ""
             :client-id ""}}}
```

Then set your client-id in `web/public/index.html`


Run the server with

`lein ring server`

## License

MIT

