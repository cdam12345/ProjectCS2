# Skiplist Collection Implementation in Java

This repository contains a custom implementation of a Skiplist collection in Java, built from scratch using generics. The collection adheres to the theoretical \( O(log n) \) runtime for common operations like `add`, `search`, and `delete`, leveraging the randomization of skiplist levels.

## Features

- **Generic Implementation**: The Skiplist is implemented using Java generics, allowing for flexibility and type safety across different data types.
- **Interfaces Implemented**:
  - `SortedSet`: The collection supports operations typically associated with sorted sets, including ordering of elements.
  - `Iterator`: Provides custom iteration functionality over the Skiplist elements.
- **Wrapper Classes**: Includes necessary wrapper classes for processing payloads, ensuring efficient and accurate handling of data within the Skiplist.
- **Constructors**: Various constructors are provided to initialize the Skiplist according to different use cases.

## Performance

The Skiplist implementation respects the theoretical \( O(log n) \) runtime for key operations by effectively randomizing the levels of the Skiplist, ensuring optimal performance for large datasets.
