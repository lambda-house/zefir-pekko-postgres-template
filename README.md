# Zefir Service Template

A [Giter8](http://www.foundweekends.org/giter8/) template for creating Zefir event-sourced backend services.

## Usage

### From Local Template

```bash
# From the zefir repository
sbt new file://zefir-service.g8

# Or with absolute path
sbt new file:///Users/timur/work/zefir/zefir-service.g8
```

### From GitHub

```bash
sbt new lambda-house/zefir-pekko-postgres-template
```

## Template Properties

| Property | Default | Description |
|----------|---------|-------------|
| name | my-service | Project name |
| organization | com.example | Organization/group ID |
| package | (derived) | Base package |
| entity | item | Starter entity name |
| zefir_group_id | tech.proxylive | Zefir Maven group |
| zefir_version | 202601.2.1-SNAPSHOT | Zefir version |
| scala_version | 3.3.7 | Scala version |
| http4s_version | 0.23.30 | Http4s version |
| circe_version | 0.14.10 | Circe version |

## Example

```bash
\$ sbt new file://zefir-service.g8
name [my-service]: order-service
organization [com.example]: com.mycompany
entity [item]: order
```

Creates a project with:
- `OrderCommand`, `OrderEvent`, `OrderState`, `OrderReply`
- `OrderBehaviour` implementing CQRS/ES
- HTTP API with `/orders/{id}` endpoints
- PostgreSQL event journal
- Pekko cluster support
