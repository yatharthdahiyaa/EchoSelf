import { useState, useEffect } from 'react'
import {
  calculateFCFS,
  calculateSJF_NonPreemptive,
  calculateSJF_Preemptive,
  calculateRoundRobin
} from './scheduler'
import './App.css'

function App() {
  const [processes, setProcesses] = useState([{ id: 1, arrival: 0, burst: 5 }])
  const [algorithm, setAlgorithm] = useState('FCFS')
  const [quantum, setQuantum] = useState(2)
  const [simulationData, setSimulationData] = useState(null)
  
  // Interactivity state
  const [hoveredProcessId, setHoveredProcessId] = useState(null)
  const [animatedTimeline, setAnimatedTimeline] = useState([])
  const [isSimulating, setIsSimulating] = useState(false)

  const handleAddProcess = () => {
    const newId = processes.length > 0 ? Math.max(...processes.map(p => p.id)) + 1 : 1
    // Auto-fill with sensible defaults based on last process
    const lastArrival = processes.length > 0 ? processes[processes.length - 1].arrival : 0;
    setProcesses([...processes, { id: newId, arrival: lastArrival + 1, burst: 2 }])
  }

  const handleRemoveProcess = (id) => {
    setProcesses(processes.filter(p => p.id !== id))
    if (hoveredProcessId === id) setHoveredProcessId(null)
  }

  const handleProcessChange = (id, field, value) => {
    setProcesses(processes.map(p => p.id === id ? { ...p, [field]: Number(value) } : p))
  }

  const handleSimulate = () => {
    if (processes.length === 0) return

    let data = null
    const procs = JSON.parse(JSON.stringify(processes)) 

    try {
      switch (algorithm) {
        case 'FCFS':
          data = calculateFCFS(procs)
          break
        case 'SJF_NP':
          data = calculateSJF_NonPreemptive(procs)
          break
        case 'SJF_P':
          data = calculateSJF_Preemptive(procs)
          break
        case 'RR':
          data = calculateRoundRobin(procs, Number(quantum))
          break
        default:
          data = calculateFCFS(procs)
      }
      setSimulationData(data)
      
      // Trigger Animation
      setIsSimulating(true)
      setAnimatedTimeline([])
      
    } catch (err) {
      console.error(err)
      alert("Error calculating simulation.")
    }
  }

  // Timeline Animation Effect
  useEffect(() => {
    if (isSimulating && simulationData) {
      const timelineLength = simulationData.timeline.length
      let currentStep = 0
      
      const interval = setInterval(() => {
        if (currentStep < timelineLength) {
          setAnimatedTimeline(prev => [...prev, simulationData.timeline[currentStep]])
          currentStep++
        } else {
          setIsSimulating(false)
          clearInterval(interval)
        }
      }, 300) // 300ms per block animation
      
      return () => clearInterval(interval)
    }
  }, [isSimulating, simulationData])


  const getProcessColor = (id) => {
    if (id === 'Idle') return 'transparent'
    const colors = [
      '#1C1E21', '#4A4D51', '#75787D', '#A1A4A9', '#2E3134'
    ]
    return colors[(id - 1) % colors.length]
  }

  return (
    <div className="layout">
      <header className="header">
        <div className="header-meta">
          <span>Process scheduling visualization</span>
          <span className="credit">Made by Yatharth Dahiya UE248110</span>
        </div>
        <h1 className="title">CPU Scheduler</h1>
      </header>

      <main className="content">
        
        {/* Left Column: Input */}
        <div className="input-panel">
          <section className="section">
            <h2 className="section-title">Configuration</h2>
            <div className="field-group">
              <label>Algorithm</label>
              <select value={algorithm} onChange={(e) => { setAlgorithm(e.target.value); setSimulationData(null); }}>
                <option value="FCFS">First Come First Serve</option>
                <option value="SJF_NP">Shortest Job First (Non-Preemptive)</option>
                <option value="SJF_P">Shortest Job First (Preemptive / SRTF)</option>
                <option value="RR">Round Robin</option>
              </select>
            </div>
            
            <div className={`time-quantum-wrapper ${algorithm === 'RR' ? 'active' : ''}`}>
              <div className="field-group">
                <label>Time Quantum</label>
                <input 
                  type="number" 
                  min="1" 
                  value={quantum} 
                  onChange={(e) => setQuantum(e.target.value)} 
                />
              </div>
            </div>
          </section>

          <section className="section">
            <div className="section-header">
              <h2 className="section-title">Processes</h2>
              <button className="text-action text-btn-add" onClick={handleAddProcess}>Add process</button>
            </div>

            <div className="process-list">
              {processes.map((p) => (
                <div 
                  className={`process-item interactive-item ${hoveredProcessId === p.id ? 'highlighted-input' : ''}`} 
                  key={p.id}
                  onMouseEnter={() => setHoveredProcessId(p.id)}
                  onMouseLeave={() => setHoveredProcessId(null)}
                >
                  <div className="process-id">P{p.id}</div>
                  <div className="process-inputs">
                    <div className="input-field">
                      <label>Arrival</label>
                      <input 
                        type="number" 
                        min="0" 
                        value={p.arrival} 
                        onChange={(e) => handleProcessChange(p.id, 'arrival', e.target.value)}
                      />
                    </div>
                    <div className="input-field">
                      <label>Burst</label>
                      <input 
                        type="number" 
                        min="1" 
                        value={p.burst} 
                        onChange={(e) => handleProcessChange(p.id, 'burst', e.target.value)}
                      />
                    </div>
                  </div>
                  <button className="icon-btn remove-btn" onClick={() => handleRemoveProcess(p.id)} aria-label="Remove process">
                    <svg viewBox="0 0 24 24" width="16" height="16" stroke="currentColor" strokeWidth="2" fill="none" strokeLinecap="round" strokeLinejoin="round">
                      <line x1="18" y1="6" x2="6" y2="18"></line>
                      <line x1="6" y1="6" x2="18" y2="18"></line>
                    </svg>
                  </button>
                </div>
              ))}
            </div>

            <button className={`primary-btn ${isSimulating ? 'simulating' : ''}`} onClick={handleSimulate} disabled={isSimulating}>
              {isSimulating ? 'Computing...' : 'Run Simulation'}
            </button>
          </section>
        </div>

        {/* Right Column: Output */}
        <div className="output-panel">
          {!simulationData ? (
            <div className="empty-state pulse-subtle">
              <svg className="empty-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1" strokeLinecap="round" strokeLinejoin="round">
                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
                <line x1="3" y1="9" x2="21" y2="9"></line>
                <line x1="9" y1="21" x2="9" y2="9"></line>
              </svg>
              <p>Configure parameters and run the simulation to view results.</p>
            </div>
          ) : (
            <div className="results fade-in">
              
              <section className="section">
                <h2 className="section-title">Timeline</h2>
                <div className="gantt-container">
                  <div className="gantt-chart">
                    {animatedTimeline.filter(Boolean).map((block, idx, arr) => {
                      const maxTime = simulationData?.timeline?.length > 0 
                        ? (simulationData.timeline[simulationData.timeline.length - 1]?.end || 1)
                        : 1;
                      const widthPercent = (((block?.end || 0) - (block?.start || 0)) / maxTime) * 100;
                      const isIdle = block?.id === 'Idle';
                      const isHovered = block && hoveredProcessId === block?.id;
                      
                      const processDetails = isIdle ? null : simulationData?.result?.find(p => p.id === block?.id);
                      
                      return (
                        <div 
                          key={idx} 
                          className={`gantt-bar slide-in-bar ${isIdle ? 'idle' : ''} ${isHovered ? 'bar-highlight' : ''} ${hoveredProcessId && !isHovered && !isIdle ? 'bar-dimmed' : ''}`}
                          style={{ 
                            width: `${widthPercent}%`, 
                            backgroundColor: isIdle ? 'transparent' : getProcessColor(block?.id),
                          }}
                          onMouseEnter={() => !isIdle && block?.id && setHoveredProcessId(block.id)}
                          onMouseLeave={() => setHoveredProcessId(null)}
                        >
                          {!isIdle && <span className="bar-label">P{block?.id}</span>}
                          
                          {/* Tooltip */}
                          {!isIdle && (
                            <div className="gantt-tooltip">
                              <div className="tooltip-header">Process {block?.id}</div>
                              <div className="tooltip-row"><span>Segment:</span> <span>{block?.start}ms - {block?.end}ms</span></div>
                              <div className="tooltip-divider"></div>
                              <div className="tooltip-row"><span>Arr:</span> <span>{processDetails?.arrival}ms</span></div>
                              <div className="tooltip-row"><span>Total Burst:</span> <span>{processDetails?.burst}ms</span></div>
                              <div className="tooltip-row"><span>Waiting:</span> <span>{processDetails?.waiting}ms</span></div>
                            </div>
                          )}

                          <span className="bar-start">{block?.start}</span>
                          {idx === arr.length - 1 && (
                            <span className="bar-end">{block?.end}</span>
                          )}
                        </div>
                      )
                    })}
                  </div>
                </div>
              </section>

              <section className="section">
                <div className="section-header">
                  <h2 className="section-title">Metrics</h2>
                  {isSimulating && <span className="simulating-badge">Calculating...</span>}
                </div>
                
                <div className="table-responsive">
                  <table className="data-table interactive-table">
                    <thead>
                      <tr>
                        <th>Process</th>
                        <th>Arrival</th>
                        <th>Burst</th>
                        <th>Completion</th>
                        <th>Turnaround</th>
                        <th>Waiting</th>
                      </tr>
                    </thead>
                    <tbody>
                      {simulationData.result.map((p) => {
                        const isHovered = hoveredProcessId === p.id;
                        return (
                          <tr 
                            key={p.id} 
                            className={`table-row ${isHovered ? 'row-highlight' : ''} ${hoveredProcessId && !isHovered ? 'row-dimmed' : ''}`}
                            onMouseEnter={() => setHoveredProcessId(p.id)}
                            onMouseLeave={() => setHoveredProcessId(null)}
                          >
                            <td><strong>P{p.id}</strong></td>
                            <td>{p.arrival}</td>
                            <td>{p.burst}</td>
                            <td>{p.completion}</td>
                            <td>{p.turnaround}</td>
                            <td>{p.waiting}</td>
                          </tr>
                        )
                      })}
                    </tbody>
                  </table>
                </div>

                <div className="summary-metrics">
                  <div className="metric">
                    <span className="metric-label">Average Turnaround</span>
                    <span className="metric-value">
                      {(simulationData.result.reduce((a, b) => a + b.turnaround, 0) / simulationData.result.length).toFixed(2)}
                      <span className="unit">ms</span>
                    </span>
                  </div>
                  <div className="metric">
                    <span className="metric-label">Average Waiting</span>
                    <span className="metric-value">
                      {(simulationData.result.reduce((a, b) => a + b.waiting, 0) / simulationData.result.length).toFixed(2)}
                      <span className="unit">ms</span>
                    </span>
                  </div>
                </div>
              </section>

            </div>
          )}
        </div>
        
      </main>
    </div>
  )
}

export default App
