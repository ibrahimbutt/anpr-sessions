# anpr-sessions

## Getting Started

This project uses Java 21, Gradle, and JUnit. Open it in IntelliJ, which
will take care of the Java version and dependencies for you.

## Inputs

Inside `./src/main/resources` I have added the example input from the test
specification. `Main` reads it by default if no argument is passed in while
running the programme. When passing the file path as an argument, a path
relative to the root of the project is expected. Alternatively, you can
replace the contents of `input.txt`, or add a new file in resources and
update the path in `Main`

## Notes

Had I more time, I would make the following
improvements:

- Extract the code in `Main` to another class,
  such as `SessionMatcher`,`SessionManager` or something like that (naming
  things is hard).
- `TrackingRecordMatcher`
    - Reduce nesting and abstract to private methods.
- `InputParser`
    - Create `InputValidator` as a dependency, to extract the validation logic.
        - An interface could be created if different format are
          expected in the future.
    - Create a custom exception, such as `InputFormatException`, for formatting
      issues.
    - Create an interface as different car parks/cameras may produce different
      formats (at scale, a service to map/normalise data might be beneficial)

### Reality

> If we were to make this algorithm more realistic and replace the body part
> of the input with real cameras sending reads to the system, what
> challenges would your system face?

In a real system where cameras are continuously sending reads to our system,
and due to the nature of the business, losing reads with no way to recover
them is our biggest concern. A loss of data/missing reads directly impacts
our revenue and that of our clients.

Broken cameras, network issues, and unplanned downtime can all result to
missing reads.

#### Broken Cameras

If the camera at entry is broken, we will have entries without an exit. I.e.
we can't charge customers. To reduce this probability, a backup camera could be
provided (e.g. closest to entry), whose reads can be used when the entry
camera stops sending reads. The same applies for exits.

Aside: A mechanism to report such issues to car parks could be built, but I'd
imagine they have something like this already. Although, it would make
things simpler on their side.

#### Network Issues

If the car parks network goes down, or they are unable to connect to our
system, what happens? Are reads first written to a local network?

What if our system goes down? What happens to the reads sent to us?

There needs to be a mechanism where the car park and our system can sync reads.

#### Unplanned Downtime

On our side, having updates without downtime is a given. On the car parks
side, it's on them, and I'd imagine they'd time such maintenance while the
car park is closed. However, if we are on AWS and it goes down, we are
missing reads, and reads might as well be hard cash. We'd want to look at
running the system on different cloud providers, either at the same time or
an approach where the other system goes online when issues are detected.

#### Issues at Scale

For a system that is reading from cameras and matching sessions at scale, I
believe the biggest concern would be processing performance due to the large
and constant amounts of reads. This of course can be improved by:

- Horizontal scaling
- Using queues, as they allow async communication, and can wait on instances of
  the session matching service during spikes, e.g. weekends, during Boxing Day
  sales, Valentine's Day, and bank holidays

##### Data

In terms of data, we need to consider data retention policies. How long are
we holding records? Keeping in mind that most sessions will last a few hours,
but there may be business or legal reasons to keep them longer. E.g. customer
disputes, legal requirements due to law enforcement, etc.

Not holding the data longer than necessary will help reduce storage costs (
including backups) and help with the performance of the database itself.

Having partitions based on regions would help with both reading and writing
due to the large amounts of tracking records. We could also offload records for
completed sessions to another database, keeping the _active_ dataset
relatively small.
