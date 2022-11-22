# KSP processor that fixes junit RetryingTest parallel execution

`@RetryingTest` force the execution onto a single thread, which is the parent node's thread. 
This has the disadvantage that several retrying tests in the same class will all run sequentially.

This is an [open issue](https://github.com/junit-pioneer/junit-pioneer/issues/276) in JUnit Pioneer project.

This project provides annotation `@ClassRetryingTest` along with
KSP processor, that automatically wrap each test into 
generated class
