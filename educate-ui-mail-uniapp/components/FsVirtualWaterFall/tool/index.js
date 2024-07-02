function rafThrottle(fn) {
    let lock = false;
    return function (...args) {
        if (lock) return;
        lock = true;
        window.requestAnimationFrame(() => {
            fn.apply(this, args);
            lock = false;
        });
    };
}

function debounce(fn, delay = 300) {
    let timer = null;
    return function (...args) {
        if (timer) clearTimeout(timer);
        timer = setTimeout(() => {
            fn.apply(this, args);
        }, delay);
    };
}

export { rafThrottle, debounce };
