# Data Set Interfaces DRAFT
**This is a draft for simpifying and extending the dataset api for the next minor release.**
**This is still work in progress and may change significantly!**

DataSets are the core concept for supplying data to charts and also to operate on the data.

## Basic Interface

A dataset in general is a table where each row corresponds to an n-dimensional point.
Data can be obtained for an index, or as a bunch operation for the complete dataset.

All bulk operations have default implementations which should be overwritten by more efficient methods if applicable.

```
Interfaces:
DataSet
```

## Structured Data

Often the individual points are not independent of each other but are structured, e.g.
on a grid.
There are some interfaces and applications which exploit this structuring and are also
used by the different renderers to decide if they are able to render a DataSet or to render it more efficiently.

```
Interfaces:
GridDataSet
EquidistantGridDataSet
(TriangularMeshDataSet?)
```

## Modifications to DataSets

Some DataSets allow to modify the Data.
As the modifications depend on the Structure of the DataSet, the class hirarchy is different, e.g. an `EditableGridDataSet` is not an instance of `EditableDataSet`.

Also the editability Interfaces provide a way to optionally specify constraints on the data.

```
Interfaces:
EditableDataSet
EditableGridDataSet
```

## Locking and Thread safety

Interface for DataSets, which allow locking, providing a way to determine if a DataSet is lockable and to obtain the lock.

Implemented via getLock() method which returns null/defaultImpl for non-lockable datasets

## Axes Descriptions (Range, Label, Unit, Dimension?)
All datasets provide axes descriptions for each dimension via `getAxesDescription(dimIndex)`.

## Errors

Specifies how DataSets provide Errors.
Each dimension specifies if it has no error, symmetrical error or asymmetrical errors.

TODO: clarify how to handle structured data. only support errors for values, not for grid?

## Histograms?

## Meta Data
Provided via DataSetMetaData interface

Q:
have metaData impementation similar to lock
getMetaData().getInfos()
getMetaData().getWarnings()
getMetaData().getMetaMap() ... etc
metaData = new DefaultMetaData();


# DataSet Implementations
- basic
    - DoubleErrorDataSet
    - DoubleDataSet
    - MultiDimDoubleDataSet
    - MultiDimDoubleErrorDataSet?
    - DoubleGridDataSet
    - +Float Types
- special data handling
    - Circular
    - Histogram(2)
    - Fragmented
    - Rolling
- math
    - Averaging
    - Transposed
    - Wrapped
    - Math DataSet (in math library)
