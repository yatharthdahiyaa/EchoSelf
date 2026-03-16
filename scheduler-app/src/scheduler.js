// Utility module for CPU Scheduling Algorithms

export function calculateFCFS(processes) {
    let current_time = 0;
    let completed = 0;
    let n = processes.length;
    let result = [];
    
    // Sort by arrival time to simulate pure FCFS
    let p = processes.map(pr => ({...pr, is_completed: false})).sort((a,b) => a.arrival - b.arrival);
    
    let timeline = [];

    for (let i = 0; i < n; i++) {
        let process = p[i];
        if (current_time < process.arrival) {
            timeline.push({ id: 'Idle', start: current_time, end: process.arrival });
            current_time = process.arrival;
        }
        let start = current_time;
        let completion = current_time + process.burst;
        let turnaround = completion - process.arrival;
        let waiting = turnaround - process.burst;
        
        timeline.push({ id: process.id, start: start, end: completion });
        
        result.push({
            id: process.id,
            arrival: process.arrival,
            burst: process.burst,
            completion,
            turnaround,
            waiting
        });
        current_time = completion;
    }
    
    return { result: result.sort((a,b) => a.id - b.id), timeline };
}

export function calculateSJF_NonPreemptive(processes) {
    let p = processes.map(pr => ({...pr, is_completed: false}));
    let n = p.length;
    let current_time = 0;
    let completed = 0;
    let result = [];
    let timeline = [];

    while (completed !== n) {
        let idx = -1;
        let min_burst = Infinity;

        for (let i = 0; i < n; i++) {
            if (p[i].arrival <= current_time && !p[i].is_completed) {
                if (p[i].burst < min_burst) {
                    min_burst = p[i].burst;
                    idx = i;
                } else if (p[i].burst === min_burst) {
                    if (idx !== -1 && p[i].arrival < p[idx].arrival) {
                        idx = i;
                    }
                }
            }
        }

        if (idx !== -1) {
            let start = current_time;
            p[idx].completion = current_time + p[idx].burst;
            p[idx].turnaround = p[idx].completion - p[idx].arrival;
            p[idx].waiting = p[idx].turnaround - p[idx].burst;
            p[idx].is_completed = true;
            
            timeline.push({ id: p[idx].id, start: start, end: p[idx].completion });
            
            result.push({...p[idx]});
            current_time = p[idx].completion;
            completed++;
        } else {
            // Check when the next process arrives to fast-forward
            let next_arrival = Infinity;
            for(let i=0; i<n; i++) {
                if(!p[i].is_completed && p[i].arrival > current_time && p[i].arrival < next_arrival) {
                    next_arrival = p[i].arrival;
                }
            }
            if (next_arrival !== Infinity) {
                timeline.push({ id: 'Idle', start: current_time, end: next_arrival });
                current_time = next_arrival;
            } else {
                current_time++;
            }
        }
    }
    return { result: result.sort((a,b)=>a.id - b.id), timeline };
}

export function calculateSJF_Preemptive(processes) {
    let p = processes.map(pr => ({...pr, is_completed: false, remaining_burst: pr.burst}));
    let n = p.length;
    let current_time = 0;
    let completed = 0;
    let result = [];
    let timeline = [];
    let last_process_id = -1;
    let current_block_start = -1;

    while (completed !== n) {
        let idx = -1;
        let min_burst = Infinity;

        for (let i = 0; i < n; i++) {
            if (p[i].arrival <= current_time && !p[i].is_completed) {
                if (p[i].remaining_burst < min_burst) {
                    min_burst = p[i].remaining_burst;
                    idx = i;
                } else if (p[i].remaining_burst === min_burst) {
                    if (idx !== -1 && p[i].arrival < p[idx].arrival) {
                        idx = i;
                    }
                }
            }
        }

        if (idx !== -1) {
            if (last_process_id !== p[idx].id) {
                if (last_process_id !== -1) {
                    timeline.push({ id: last_process_id, start: current_block_start, end: current_time });
                }
                current_block_start = current_time;
                last_process_id = p[idx].id;
            }

            p[idx].remaining_burst -= 1;
            current_time++;

            if (p[idx].remaining_burst === 0) {
                p[idx].completion = current_time;
                p[idx].turnaround = p[idx].completion - p[idx].arrival;
                p[idx].waiting = p[idx].turnaround - p[idx].burst;
                p[idx].is_completed = true;
                result.push({...p[idx]});
                completed++;
                
                timeline.push({ id: last_process_id, start: current_block_start, end: current_time });
                last_process_id = -1;
            }
        } else {
            if (last_process_id !== 'Idle') {
                if (last_process_id !== -1) {
                    timeline.push({ id: last_process_id, start: current_block_start, end: current_time });
                }
                current_block_start = current_time;
                last_process_id = 'Idle';
            }
            current_time++;
        }
    }
    return { result: result.sort((a,b)=>a.id - b.id), timeline };
}

export function calculateRoundRobin(processes, quantum) {
    let p = processes.map(pr => ({...pr, is_completed: false, remaining_burst: pr.burst})).sort((a,b) => a.arrival - b.arrival);
    let n = p.length;
    let current_time = 0;
    let completed = 0;
    let result = [];
    let timeline = [];
    
    let queue = [];
    let in_queue = Array(n).fill(false);
    
    // Start with the first process(es)
    if (p.length > 0) {
        current_time = p[0].arrival;
        queue.push(0);
        in_queue[0] = true;
    }

    while (completed !== n) {
        if (queue.length === 0) {
            // Find next process to arrive
            let next_arrival = Infinity;
            for(let i=0; i<n; i++) {
                if(!p[i].is_completed && p[i].arrival > current_time && p[i].arrival < next_arrival) {
                    next_arrival = p[i].arrival;
                }
            }
            if(next_arrival !== Infinity) {
                timeline.push({ id: 'Idle', start: current_time, end: next_arrival });
                current_time = next_arrival;
                for (let i = 0; i < n; i++) {
                    if (p[i].arrival <= current_time && !p[i].is_completed && !in_queue[i]) {
                        queue.push(i);
                        in_queue[i] = true;
                    }
                }
            }
            continue;
        }

        let idx = queue.shift();
        in_queue[idx] = false;

        let execute_time = Math.min(p[idx].remaining_burst, quantum);
        timeline.push({ id: p[idx].id, start: current_time, end: current_time + execute_time });
        
        p[idx].remaining_burst -= execute_time;
        current_time += execute_time;

        for (let i = 0; i < n; i++) {
            if (p[i].arrival <= current_time && !p[i].is_completed && !in_queue[i] && i !== idx) {
                if (p[i].remaining_burst > 0) {
                    queue.push(i);
                    in_queue[i] = true;
                }
            }
        }

        if (p[idx].remaining_burst === 0) {
            p[idx].completion = current_time;
            p[idx].turnaround = p[idx].completion - p[idx].arrival;
            p[idx].waiting = p[idx].turnaround - p[idx].burst;
            p[idx].is_completed = true;
            result.push({...p[idx]});
            completed++;
        } else {
            queue.push(idx);
            in_queue[idx] = true;
        }
    }
    
    // We could compact adjacent timeline entries here but they are handled by the React Gantt rendering
    return { result: result.sort((a,b)=>a.id - b.id), timeline };
}
