import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

def scale_values(values: list, new_min: float, new_max: float):
    values = np.array(values)

    min_val = np.min(values)
    max_val = np.max(values)

    factor1 = new_min / min_val
    factor2 = new_max / max_val

    return (factor1 + factor2) / 2

def process_time_values(df: pd.DataFrame, time_column: str = "Time"):
    time_values = df[time_column] - df[time_column].min()
    time_values = time_values * 1000000
    time_values = time_values.apply(lambda x: int(x))
    return time_values

def plot_through_put_line(
    time_values,
    rsocket_throughput: pd.DataFrame = None,
    grpc_throughput: pd.DataFrame = None,
    y_max=None,
    y_min=None,
    labels=None,
):
    x_max = max(time_values)
    x_min = min(time_values)

    split = np.linspace(x_min, x_max, 5)
    ranges = list(zip(split[0:-1], split[1:]))
    x_pos_throughput = [range[0] + (range[1] - range[0]) / 2 for range in ranges]

    rsocket_throughput_values = rsocket_throughput["Score"]
    grpc_throughput_values = grpc_throughput["Score"]

    factor = 1

    if y_max is not None and x_max is not None:
        factor = scale_values(rsocket_throughput_values, 1.2 * y_min, 0.7 * y_max)

    plt.plot(
        x_pos_throughput,
        list(map(lambda x: factor * x, rsocket_throughput_values)),
        color="red",
        linestyle="--",
        marker = "o",
        label="RSocket Troughput",
    )
    plt.plot(
        x_pos_throughput,
        list(map(lambda x: factor * x, grpc_throughput_values)),
        color="blue",
        linestyle="--",
        marker='o',
        label="gRPC Throughput",
    )

    if labels == None:
        plt.xticks(x_pos_throughput)
    else:
        plt.xticks(x_pos_throughput, labels)

def draw_throughput_chart(
    rsocket_df: pd.DataFrame, grpc_df: pd.DataFrame, thread_stats
):
    rsocket_throughput_values = rsocket_df["Score"]
    grpc_throughput_values = grpc_df["Score"]

    width = 0.35
    plt.bar(
        [i - width / 2 for i in range(len(rsocket_throughput_values))],
        rsocket_throughput_values,
        color="red",
        label="RSocket",
        width=width,
        alpha=0.7,
    )
    plt.bar(
        [i + width / 2 for i in range(len(grpc_throughput_values))],
        grpc_throughput_values,
        color="blue",
        label="gRPC",
        width=width,
        alpha=0.7,
    )

    plt.xticks([i for i in range(len(rsocket_throughput_values))], thread_stats)
    plt.xlabel("Num of concurrent clients")
    plt.ylabel("Throughput")
    plt.legend()
    plt.show()


def draw_cpu_chart(
    rsocket_df: pd.DataFrame,
    grpc_df: pd.DataFrame,
    rsocket_throughput: pd.DataFrame = None,
    grpc_throughput: pd.DataFrame = None,
    throughput_labels=None,
    title="CPU Usage chart"
):
    rsocket_time_values = process_time_values(rsocket_df)

    rsocket_cpu_coordinates_x = list(rsocket_time_values)
    rsocket_cpu_coordinates_y = list(rsocket_df["CPU Usage"])
    rsocket_cpu_coordinates_y_s = np.convolve(
        rsocket_cpu_coordinates_y, np.ones(3) / 3, mode="same"
    )

    plt.plot(
        rsocket_cpu_coordinates_x,
        rsocket_cpu_coordinates_y,
        color="red",
        label="RSocket",
        alpha=0.6,
    )

    grpc_time_values = process_time_values(grpc_df)

    grpc_cpu_coordinates_x = list(grpc_time_values)
    grpc_cpu_coordinates_y = list(grpc_df["CPU Usage"])
    grpc_cpu_coordinates_y_s = np.convolve(
        grpc_cpu_coordinates_y, np.ones(3) / 3, mode="same"
    )

    plt.plot(
        grpc_cpu_coordinates_x,
        grpc_cpu_coordinates_y,
        color="blue",
        label="gRPC",
        alpha=0.6,
    )

    if grpc_throughput is not None and rsocket_throughput is not None:
        plot_through_put_line(
            grpc_time_values,
            rsocket_throughput,
            grpc_throughput,
            max(grpc_cpu_coordinates_y),
            min(grpc_cpu_coordinates_y),
            labels=throughput_labels,
        )

    plt.title(title)
    plt.legend()
    plt.xlabel("Time")
    plt.ylabel("CPU Usage")
    plt.show()

def draw_thread_chart(
    rsocket_df: pd.DataFrame,
    grpc_df: pd.DataFrame,
    rsocket_throughput: pd.DataFrame = None,
    grpc_throughput: pd.DataFrame = None,
    throughput_labels=None,
    title = "Live Thread Chart"
):
    rsocket_time_values = process_time_values(rsocket_df)

    rsocket_thread_coordinates_x = list(rsocket_time_values)
    rsocket_thread_coordinates_y = list(rsocket_df["Live threads"])
    rsocket_thread_coordinates_y_s = np.convolve(
        rsocket_thread_coordinates_y, np.ones(3) / 3, mode="same"
    )

    grpc_time_values = process_time_values(grpc_df)

    grpc_thread_coordinates_x = list(grpc_time_values)
    grpc_thread_coordinates_y = list(grpc_df["Live threads"])
    grpc_thread_coordinates_y_s = np.convolve(
        grpc_thread_coordinates_y, np.ones(3) / 3, mode="same"
    )

    plt.plot(
        grpc_thread_coordinates_x,
        grpc_thread_coordinates_y,
        color="blue",
        label="gRPC",
        alpha=0.6,
    )
    plt.plot(
        rsocket_thread_coordinates_x,
        rsocket_thread_coordinates_y,
        color="red",
        label="RSocket",
        alpha=0.6,
    )

    if grpc_throughput is not None and rsocket_throughput is not None:
        plot_through_put_line(
            grpc_time_values,
            rsocket_throughput,
            grpc_throughput,
            max(grpc_thread_coordinates_y),
            min(grpc_thread_coordinates_y),
            labels=throughput_labels,
        )

    plt.legend()
    plt.xlabel("Time")
    plt.ylabel("Live threads")
    plt.title(title)
    plt.show()

def draw_mem_chart(
    rsocket_df: pd.DataFrame,
    grpc_df: pd.DataFrame,
    window: int = 9,
    rsocket_throughput: pd.DataFrame = None,
    grpc_throughput: pd.DataFrame = None,
    throughput_labels=None,
    title="Memory Usage Chart"
):
    rsocket_time_values = process_time_values(rsocket_df)

    rsocket_mem_coordinates_x = list(rsocket_time_values)
    rsocket_mem_coordinates_y = list(rsocket_df["Used"])
    rsocket_mem_coordinates_y_s = np.convolve(
        rsocket_mem_coordinates_y, np.ones(window) / window, mode="same"
    )

    grpc_time_values = process_time_values(grpc_df)

    grpc_mem_coordinates_x = list(grpc_time_values)
    grpc_mem_coordinates_y = list(grpc_df["Used"])
    grpc_mem_coordinates_y_s = np.convolve(
        grpc_mem_coordinates_y, np.ones(window) / window, mode="same"
    )

    plt.plot(
        grpc_mem_coordinates_x,
        grpc_mem_coordinates_y_s,
        color="blue",
        label="gRPC",
        alpha=0.6,
    )
    plt.plot(
        rsocket_mem_coordinates_x,
        rsocket_mem_coordinates_y_s,
        color="red",
        label="RSocket",
        alpha=0.6,
    )

    if grpc_throughput is not None and rsocket_throughput is not None:
        plot_through_put_line(
            grpc_time_values,
            rsocket_throughput,
            grpc_throughput,
            max(grpc_mem_coordinates_y),
            min(grpc_mem_coordinates_y),
            labels=throughput_labels,
        )

    plt.legend()
    plt.xlabel("Time")
    plt.ylabel("Used Memory")
    plt.title(title)
    plt.show()