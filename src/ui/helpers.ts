
export const roundToNearestN = (input: number, n: number) => {
    return Math.round(input / n) * n;
}