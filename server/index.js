// server/index.js

import express from 'express'
import cors from 'cors'
import { Low } from 'lowdb'
import { JSONFile } from 'lowdb/node'
import { nanoid } from 'nanoid'

// Initialize LowDB with JSON file
const adapter = new JSONFile('server/db.json')
const db = new Low(adapter)
await db.read()
// If db is empty, set default structure
if (!db.data) {
  db.data = {
    users: [],
    roles: [],
    products: [],
    orders: [],
    purchases: [],
    inventory: [],
    quotations: [],
    customers: [],
    suppliers: []
  }
  await db.write()
}

const app = express()
app.use(cors())
app.use(express.json())

// Middleware: validate collection param
app.param('collection', (req, res, next, collection) => {
  if (!db.data[collection]) {
    return res.status(404).json({ message: `Collection '${collection}' not found.` })
  }
  req.collection = collection
  next()
})

// List items with basic pagination
app.get('/api/:collection', (req, res) => {
  const list = db.data[req.collection]
  const page = parseInt(req.query.page) || 1
  const size = parseInt(req.query.size) || list.length
  const start = (page - 1) * size
  const end = start + size
  const data = list.slice(start, end)
  res.json({ data, total: list.length })
})

// Retrieve single item
app.get('/api/:collection/:id', (req, res) => {
  const item = db.data[req.collection].find(i => i.id === req.params.id)
  if (!item) return res.status(404).json({ message: 'Not found' })
  res.json(item)
})

// Create new item
app.post('/api/:collection', async (req, res) => {
  const item = { id: nanoid(), ...req.body }
  db.data[req.collection].push(item)
  await db.write()
  res.status(201).json(item)
})

// Update item
app.put('/api/:collection/:id', async (req, res) => {
  const idx = db.data[req.collection].findIndex(i => i.id === req.params.id)
  if (idx === -1) return res.status(404).json({ message: 'Not found' })
  db.data[req.collection][idx] = { ...db.data[req.collection][idx], ...req.body }
  await db.write()
  res.json(db.data[req.collection][idx])
})

// Delete item
app.delete('/api/:collection/:id', async (req, res) => {
  const idx = db.data[req.collection].findIndex(i => i.id === req.params.id)
  if (idx === -1) return res.status(404).json({ message: 'Not found' })
  const [removed] = db.data[req.collection].splice(idx, 1)
  await db.write()
  res.json(removed)
})

const PORT = process.env.PORT || 4000
app.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}`)
})