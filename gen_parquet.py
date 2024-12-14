import pandas as pd
import pyarrow as pa
from pyarrow import parquet

# Define schema explicitly
schema = pa.schema([
    ('name', pa.string()),
    ('coordinates_x', pa.float64()),
    ('coordinates_y', pa.float64()),
    ('eyeColor', pa.string()),
    ('hairColor', pa.string()),
    ('location_x', pa.float64()),
    ('location_y', pa.float64()),
    ('location_z', pa.float64()),
    ('height', pa.float64()),
    ('birthday', pa.string()),
    ('nationality', pa.string())
])

# Create data for 2 persons
data = {
    'name': ['John Doe', 'Jane Smith'],
    'coordinates_x': [100.0, 200.0],
    'coordinates_y': [10.5, 20.5],
    'eyeColor': ['BLACK', 'BLUE'],
    'hairColor': ['BLACK', 'ORANGE'],
    'location_x': [1000.0, 2000.0],
    'location_y': [15.5, 25.5],
    'location_z': [5.5, 7.5],
    'height': [180.5, 165.5],
    'birthday': [
        '1990-01-01T00:00:00Z',
        '1995-06-15T00:00:00Z'
    ],
    'nationality': ['RUSSIA', 'FRANCE']
}

# Create DataFrame
df = pd.DataFrame(data)

# Convert to Table with explicit schema
table = pa.Table.from_pandas(df, schema=schema)

# Write with specific configuration
parquet.write_table(
    table, 
    'persons.parquet',
    compression='snappy',
    version='2.6'
)
