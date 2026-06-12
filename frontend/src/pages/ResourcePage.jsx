import { Alert, Box, Button, Card, CardContent, Grid, MenuItem, Stack, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from '@mui/material';
import Add from '@mui/icons-material/Add';
import Delete from '@mui/icons-material/Delete';
import Refresh from '@mui/icons-material/Refresh';
import Save from '@mui/icons-material/Save';
import { useEffect, useMemo, useState } from 'react';
import { api, unwrap } from '../api/client';

export default function ResourcePage({ title, endpoint, fields, columns, initial, query = '' }) {
  const [rows, setRows] = useState([]);
  const [form, setForm] = useState(initial);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState('');

  const url = useMemo(() => `${endpoint}${query}`, [endpoint, query]);
  const load = async () => setRows(unwrap(await api.get(url)) || []);

  useEffect(() => { load().catch(() => setRows([])); }, [url]);

  const save = async () => {
    const payload = Object.fromEntries(Object.entries(form).map(([key, value]) => [key, value === '' ? null : value]));
    if (editingId) {
      await api.put(`${endpoint}/${editingId}`, payload);
      setMessage(`${title} updated`);
    } else {
      await api.post(endpoint, payload);
      setMessage(`${title} created`);
    }
    setForm(initial);
    setEditingId(null);
    await load();
  };

  const remove = async (id) => {
    await api.delete(`${endpoint}/${id}`);
    await load();
  };

  return (
    <Stack spacing={3}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', gap: 2, alignItems: 'center' }}>
        <Box><Typography variant="h4">{title}</Typography><Typography color="text.secondary">Create, search, update, and remove records.</Typography></Box>
        <Button startIcon={<Refresh />} onClick={load}>Refresh</Button>
      </Box>
      {message && <Alert severity="success" onClose={() => setMessage('')}>{message}</Alert>}
      <Card>
        <CardContent>
          <Grid container spacing={2}>
            {fields.map((field) => (
              <Grid item xs={12} sm={6} md={4} key={field.name}>
                <TextField
                  select={Boolean(field.options)}
                  label={field.label}
                  type={field.type || 'text'}
                  value={form[field.name] ?? ''}
                  onChange={(e) => setForm({ ...form, [field.name]: field.type === 'number' ? Number(e.target.value) : e.target.value })}
                  fullWidth
                >
                  {field.options?.map((option) => <MenuItem key={option} value={option}>{option}</MenuItem>)}
                </TextField>
              </Grid>
            ))}
            <Grid item xs={12}>
              <Button startIcon={editingId ? <Save /> : <Add />} variant="contained" onClick={save}>{editingId ? 'Update' : 'Create'}</Button>
            </Grid>
          </Grid>
        </CardContent>
      </Card>
      <Card>
        <Table>
          <TableHead><TableRow>{columns.map((col) => <TableCell key={col.key}>{col.label}</TableCell>)}<TableCell align="right">Actions</TableCell></TableRow></TableHead>
          <TableBody>
            {rows.map((row) => (
              <TableRow key={row.id} hover onClick={() => { setEditingId(row.id); setForm({ ...initial, ...row }); }}>
                {columns.map((col) => <TableCell key={col.key}>{String(row[col.key] ?? '')}</TableCell>)}
                <TableCell align="right"><Button color="error" startIcon={<Delete />} onClick={(e) => { e.stopPropagation(); remove(row.id); }}>Delete</Button></TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Card>
    </Stack>
  );
}
