import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

'''
Some functions to plot and visualise cpu, memory and thread utilisation
from a JConsole generated CSV file
'''

def parse_to_standard(path: str):
    '''
    Function to parse a VisualVM trace to a pandas dataframe similar to the one 
    obtained from a JConsole CSV value
    '''
    common_df = pd.read_csv(path)
    cpu_df = pd.DataFrame(common_df["CPU usage [%]"])
    cpu_df.columns = ["CPU Usage"]
    cpu_df["Time"] = cpu_df.index
    
    mem_df = pd.DataFrame(common_df["Used [B]"].apply(lambda x: int(x.replace(",", ""))/1e6))
    mem_df.columns = ["Used"]
    mem_df["Time"] = mem_df.index
    
    thread_df = pd.DataFrame(common_df["Live"])
    thread_df.columns = ["Live threads"]
    thread_df["Time"] = thread_df.index

    return (cpu_df, mem_df, thread_df)

def scale_values(values: list, new_min: float, new_max: float):
    ''' Function that scales values to be between two limits while preserving order '''
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
    ''' Function to superimpose throughput values on CPU/MEM/Thread utilisation charts'''
    font = {'family': 'Proxima Nova', 'size': 12}
    plt.rc('font', **font)
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
        color=[219/255,50/255,143/255],
        linestyle="--",
        marker = "o",
        label="RSocket Troughput",
    )
    plt.plot(
        x_pos_throughput,
        list(map(lambda x: factor * x, grpc_throughput_values)),
        color=[100/255,189/255,189/255],
        linestyle="--",
        marker='o',
        label="gRPC Throughput",
    )

    if labels == None:
        plt.xticks(x_pos_throughput)
    else:
        plt.xticks(x_pos_throughput, labels)


def draw_throughput_chart(
    rsocket_df: pd.DataFrame,
    grpc_df: pd.DataFrame,
    thread_stats,
    title: str = "Thoughput",
    save_loc="./graphs",
):
    ''' Function to draw a throughput chart and store the output to saveloc'''
    font = {"family": "Proxima Nova", "size": 12}
    plt.rc("font", **font)
    rsocket_throughput_values = rsocket_df["Score"]
    grpc_throughput_values = grpc_df["Score"]

    width = 0.25
    plt.barh(
        [i - width / 2 for i in range(len(rsocket_throughput_values))],
        rsocket_throughput_values,
        color=[219 / 255, 50 / 255, 143 / 255],
        label="RSocket",
        height=width,
    )
    plt.barh(
        [i + width / 2 for i in range(len(grpc_throughput_values))],
        grpc_throughput_values,
        color=[100 / 255, 189 / 255, 189 / 255],
        label="gRPC",
        height=width,
    )

    plt.yticks([i for i in range(len(rsocket_throughput_values))], thread_stats)
    plt.ylabel("Num of Threads")
    plt.xlabel("Throughput")
    plt.legend()
    plt.title(title)
    plt.tight_layout()
    plt.savefig(save_loc)
    plt.show()

def draw_cpu_chart(
    rsocket_df: pd.DataFrame,
    grpc_df: pd.DataFrame,
    rsocket_throughput: pd.DataFrame = None,
    grpc_throughput: pd.DataFrame = None,
    throughput_labels=None,
    title="CPU Usage chart",
    save_loc = "./graphs"
):
    ''' Function to draw a CPU utilisation chart and store the output to saveloc'''
    
    font = {'family': 'Proxima Nova', 'size': 12}
    plt.rc('font', **font)
    rsocket_time_values = process_time_values(rsocket_df)

    rsocket_cpu_coordinates_x = list(rsocket_time_values)
    rsocket_cpu_coordinates_y = list(rsocket_df["CPU Usage"])
    rsocket_cpu_coordinates_y_s = np.convolve(
        rsocket_cpu_coordinates_y, np.ones(3) / 3, mode="same"
    )

    plt.plot(
        rsocket_cpu_coordinates_x,
        rsocket_cpu_coordinates_y,
        color=[219/255,50/255,143/255],
        label="RSocket",
        alpha=0.8,
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
        color=[100/255,189/255,189/255],
        label="gRPC",
        alpha=0.8,
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
    plt.legend(loc='upper left')
    plt.xlabel("Time")
    plt.ylabel("CPU Usage")
    plt.tight_layout()
    plt.savefig(save_loc)
    plt.show()


def draw_thread_chart(
    rsocket_df: pd.DataFrame,
    grpc_df: pd.DataFrame,
    rsocket_throughput: pd.DataFrame = None,
    grpc_throughput: pd.DataFrame = None,
    throughput_labels=None,
    title = "Live Thread Chart",
    save_loc = "./graphs"
):
    ''' Function to draw a thread utilisation chart and store the output to saveloc'''
    
    font = {'family': 'Proxima Nova', 'size': 12}
    plt.rc('font', **font)
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
        color=[100/255,189/255,189/255],
        label="gRPC",
        alpha=0.8,
    )
    plt.plot(
        rsocket_thread_coordinates_x,
        rsocket_thread_coordinates_y,
        color=[219/255,50/255,143/255],
        label="RSocket",
        alpha=0.8,
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

    plt.legend(loc='upper left')
    plt.xlabel("Time")
    plt.ylabel("Live threads")
    plt.title(title)
    plt.tight_layout()

    plt.savefig(save_loc)
    plt.show()

def draw_mem_chart(
    rsocket_df: pd.DataFrame,
    grpc_df: pd.DataFrame,
    window: int = 9,
    rsocket_throughput: pd.DataFrame = None,
    grpc_throughput: pd.DataFrame = None,
    throughput_labels=None,
    title="Memory Usage Chart",
    save_loc = "./graphs"
):
    ''' Function to draw a memory utilisation chart and store the output to saveloc'''
    
    font = {'family': 'Proxima Nova', 'size': 12}
    plt.rc('font', **font)
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
        color=[100/255,189/255,189/255],
        label="gRPC",
        alpha=0.8,
    )
    plt.plot(
        rsocket_mem_coordinates_x,
        rsocket_mem_coordinates_y_s,
        color=[219/255,50/255,143/255],
        label="RSocket",
        alpha=0.8,
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

    plt.legend(loc='upper right', bbox_to_anchor=(1.1, 1))
    plt.xlabel("Time")
    plt.ylabel("Used Memory")
    plt.title(title)
    plt.tight_layout()
    plt.savefig(save_loc)
    plt.show()